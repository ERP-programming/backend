package ac.su.erp.service;

import ac.su.erp.constant.DepartmentPosition;
import ac.su.erp.constant.EmployeePosition;
import ac.su.erp.constant.EmploymentStatus;
import ac.su.erp.domain.Bank;
import ac.su.erp.domain.Department;
import ac.su.erp.domain.Employee;
import ac.su.erp.dto.ContractUpdateForm;
import ac.su.erp.dto.EmployeeCreateForm;
import ac.su.erp.dto.EmployeeUpdateForm;
import ac.su.erp.dto.SpringUser;
import ac.su.erp.repository.BankRepository;
import ac.su.erp.repository.DepartmentRepository;
import ac.su.erp.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    //은행 찾기
    private Bank getBankByCode(String bankCode) {
        return bankRepository.findByBankCode(bankCode)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 은행 코드입니다."));
    }

    //부서 찾기
    private Department getDepartmentByNo(Long deptNo) {
        return departmentRepository.findById(deptNo)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 부서 번호입니다."));
    }

    //사원 등록
    @Transactional
    public void createEmployee(EmployeeCreateForm form) {
        Employee employee = new Employee();
        employee.setEmpNum(form.getEmpNum());
        employee.setEmpBirth(form.getEmpBirth());
        employee.setEmpDel(EmploymentStatus.EMPLOYED);
        employee.setEmpHead(DepartmentPosition.DepartmentMember);
        employee.setEmpName(form.getEmpName());
        employee.setEmpGender(form.getEmpGender());
        employee.setEmpBirthNum(form.getEmpBirthNum());
        employee.setEmpPnum(form.getEmpPnum());
        employee.setEmpAddr(form.getEmpAddr());
        employee.setEmpPos(EmployeePosition.STAFF);
        employee.setEmpPw(passwordEncoder.encode("1234"));
        employee.setEmpEmail(form.getEmpEmail());
        employee.setStartDay(LocalDate.now());
        employee.setEmpBanknum(form.getEmpBanknum());

        employee.setBank(getBankByCode(form.getBankCode()));
        employee.setDepartment(getDepartmentByNo(form.getDeptNo()));
        employee.setConIncome(form.getSalary());
        employee.setConStartday(form.getContractStart());
        employee.setConEndday(form.getContractEnd());
        employeeRepository.save(employee);

    }

    // 사원 번호로 사원 찾기
    public Optional<Employee> findByEmployeeNum(Long empNum) {
        return employeeRepository.findByEmpNum(empNum);
    }

    // 모든 사원 찾기
    public List<Employee> findEmployedEmployees() {
        return employeeRepository.findByEmpDel(EmploymentStatus.EMPLOYED);
    }

    // 사원 인사 정보 변경
    public void updateEmployee(EmployeeUpdateForm form) {
        Employee employee = employeeRepository.findByEmpNum(form.getEmpNum())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        Department department=departmentRepository.findByDeptNo(form.getDeptNo())
                .orElseThrow(() -> new EntityNotFoundException("Department not found"));
        employee.setDepartment(department);
        employee.setEmpPos(form.getEmpPos());
        employee.setEmpHead(form.getEmpHead());

        employeeRepository.save(employee);
    }

    // 계약 정보 변경
    public void updateContract(ContractUpdateForm form) {
        Employee employee = employeeRepository.findByEmpNum(form.getEmpNum())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setConIncome(form.getSalary());
        employee.setConStartday(form.getContractStart());
        employee.setConEndday(form.getContractEnd());
        employee.setEmpBanknum(form.getEmpBanknum());
        employee.setBank(getBankByCode(form.getBankCode()));

        employeeRepository.save(employee);
    }

//    //비밀번호 변경
//    @Transactional
//    public void changePassword(Long empNum, String newPassword) {
//        Optional<Employee> employeeOptional = employeeRepository.findByEmpNum(empNum);
//        if (employeeOptional.isPresent()) {
//            Employee employee = employeeOptional.get();
//            employee.setEmpPw(passwordEncoder.encode(newPassword));
//            employeeRepository.save(employee);
//        } else {
//            throw new UsernameNotFoundException("User not found with empNum: " + empNum);
//        }
//    }

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


}
