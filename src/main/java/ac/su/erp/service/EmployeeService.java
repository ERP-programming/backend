package ac.su.erp.service;

import ac.su.erp.constant.DepartmentPosition;
import ac.su.erp.constant.EmployeePosition;
import ac.su.erp.constant.EmploymentStatus;
import ac.su.erp.constant.GenderEnum;
import ac.su.erp.domain.Bank;
import ac.su.erp.domain.Contract;
import ac.su.erp.domain.Department;
import ac.su.erp.domain.Employee;
import ac.su.erp.dto.EmployeeCreateForm;
import ac.su.erp.dto.SpringUser;
import ac.su.erp.repository.BankRepository;
import ac.su.erp.repository.ContractRepository;
import ac.su.erp.repository.DepartmentRepository;
import ac.su.erp.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ac.su.erp.constant.EmploymentStatus.RESIGNED;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final BankRepository bankRepository;
    private final ContractRepository contractRepository;
    private final PasswordEncoder passwordEncoder;
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> registeredEmployee = employeeRepository.findByEmpNum(Long.parseLong(username));
        if (registeredEmployee.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return SpringUser.getSpringUserDetails(registeredEmployee.get());
    }

    public EmployeeCreateForm createEmployee(EmployeeCreateForm employeeCreateForm) {
        Employee newEmployee = new Employee();
        newEmployee.setEmpNum(employeeCreateForm.getEmpNum());
        newEmployee.setEmpPw(passwordEncoder.encode("1234")); // 기본값 "1234"를 암호화하여 저장
        newEmployee.setEmpName(employeeCreateForm.getEmpName());
        newEmployee.setEmpBirth(employeeCreateForm.getEmpBirth());
        newEmployee.setEmpBirthNum(employeeCreateForm.getEmpBirthNum());
        newEmployee.setEmpHead(employeeCreateForm.getEmpHead());
        newEmployee.setEmpGender(employeeCreateForm.getEmpGender());
        newEmployee.setEmpPnum(employeeCreateForm.getEmpPnum());
        newEmployee.setEmpAddr(employeeCreateForm.getEmpAddr());
        newEmployee.setEmpPos(employeeCreateForm.getEmpPos());
        newEmployee.setEmpEmail(employeeCreateForm.getEmpEmail());
        newEmployee.setStartDay(employeeCreateForm.getStartDay());
        newEmployee.setEmpBanknum(employeeCreateForm.getEmpBanknum());

        // Bank와 Department를 조회하여 설정
        Bank bank = bankRepository.findByBankCode(String.valueOf(employeeCreateForm.getBankCode()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid bank code: " + employeeCreateForm.getBankCode()));
        newEmployee.setBankCode(bank);

        Department department = departmentRepository.findById(employeeCreateForm.getDeptNo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid department number: " + employeeCreateForm.getDeptNo()));
        newEmployee.setDeptNo(department);

        validateDuplicateEmployee(newEmployee);
        employeeRepository.save(newEmployee);
        return employeeCreateForm;
    }

    public Optional<Employee> findByEmployeeNum(Long empNum) {
        return employeeRepository.findByEmpNum(empNum);
    }

    public List<Employee> findByEmployeeName(String empName) {
        return employeeRepository.findByEmpName(empName);
    }

    public List<Employee> findResignedEmployees() {
        return employeeRepository.findByEmpDel(EmploymentStatus.EMPLOYED);
    }


    //비밀번호 변경
    @Transactional
    public void changePassword(Long empNum, String newPassword) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmpNum(empNum);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setEmpPw(passwordEncoder.encode(newPassword));
            employeeRepository.save(employee);
        } else {
            throw new UsernameNotFoundException("User not found with empNum: " + empNum);
        }
    }

    // 사원 인사 정보 변경
    @Transactional
    public void changeEmployeeInfo(Long empNum, EmployeeCreateForm form) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmpNum(empNum);
            Employee employee = employeeOptional.get();
            employee.setDepartment(departmentRepository.findById(form.getDeptNo()).get());
            employeeRepository.save(employee);

    }

    // 사원 퇴사 처리
    public void resignEmployee(Long empNum) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmpNum(empNum);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setEndDay(LocalDate.now());
            employee.setEmpDelInfo("사원 본인 퇴사");
            employee.setEmpDel(RESIGNED);
            employee.setEmpPw(passwordEncoder.encode(generateRandomPassword())); //비밀번호 랜덤으로 변경
            employeeRepository.save(employee);
            deleteEmployeeSessions(employee.getEmpNum().toString()); // 사원 세션 삭제

        } else {
            throw new UsernameNotFoundException("User not found with empNum: " + empNum);
        }
    }

    private String generateRandomPassword() {
        return UUID.randomUUID().toString();
    }

    private void deleteEmployeeSessions(String username) {
        sessionRepository.findByPrincipalName(username).forEach((sessionId, session) -> {
            sessionRepository.deleteById(sessionId);
        });
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void updateEmployee(Long empNum, EmployeeCreateForm form) {
        // 사원 정보 업데이트
        Optional<Employee> employeeOptional = employeeRepository.findByEmpNum(empNum);

    }
}
