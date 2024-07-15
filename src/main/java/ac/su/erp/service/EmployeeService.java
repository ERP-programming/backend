package ac.su.erp.service;

import ac.su.erp.constant.GenderEnum;
import ac.su.erp.domain.Bank;
import ac.su.erp.domain.Department;
import ac.su.erp.domain.Employee;
import ac.su.erp.dto.EmployeeCreateForm;
import ac.su.erp.dto.SpringUser;
import ac.su.erp.repository.BankRepository;
import ac.su.erp.repository.DepartmentRepository;
import ac.su.erp.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

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

    public void createEmployee(Employee employee) {
        // 주민등록번호로 생년월일, 성별, 나이 자동 입력
        String birthNum = employee.getEmpBirthNum();
        employee.setEmpBirthNum(birthNum);
        employee.setEmpBirth(getBirthNumAsInt(birthNum));
        employee.setEmpGender(parseGender(birthNum));
        employee.setEmpPw(passwordEncoder.encode(parsePassword(birthNum)));
        employee.setEmpAge(LocalDate.now().getYear() - employee.getEmpBirth());
        // 나머지 기본값 자동 설정
        employee.setEmpInfoChange(LocalDate.now());
        employeeRepository.save(employee);
    }

    private String parsePassword(String birthNum) {
        // 생년월일로 비밀번호 생성하는 로직 작성
        return birthNum.substring(0, 6); // 예시 코드
    }
    public Long getBirthNumAsInt(String birthNum) {
        // 첫 6자를 추출
        String birthNumSubstring = birthNum.substring(0, 6);
        // 문자열을 정수로 변환(생년월일)
        return (Long) (long) Integer.parseInt(birthNumSubstring);
    }

    private GenderEnum parseGender(String birthNum) {
        // 주민등록번호로 성별 파싱하는 로직 작성
        return GenderEnum.MALE; // 예시 코드
    }



    public Optional<Employee> findByEmployeeNum(Long empNum) {
        return employeeRepository.findByEmpNum(empNum);
    }

    public List<Employee> findByEmployeeName(String empName) {
        return employeeRepository.findByEmpName(empName);
    }
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }



    public void logout(Long empNum) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmpNum(empNum);
        if (employeeOptional.isPresent()) {
            //사용자 로그아웃 처리 (세션 삭제)
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        } else {
            throw new UsernameNotFoundException("User not found with empNum: " + empNum);
        }
    }

    //비밀번호 변경
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
