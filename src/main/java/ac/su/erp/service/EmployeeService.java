package ac.su.erp.service;

import ac.su.erp.config.jwt.TokenProvider;
import ac.su.erp.domain.Bank;
import ac.su.erp.domain.Department;
import ac.su.erp.domain.Employee;
import ac.su.erp.dto.EmployeeCreateForm;
import ac.su.erp.dto.LoginResponseDTO;
import ac.su.erp.repository.BankRepository;
import ac.su.erp.repository.DepartmentRepository;
import ac.su.erp.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final BankRepository bankRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

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

    public boolean existsByEmpNum(Long empNum) {
        return employeeRepository.existsByEmpNum(empNum);
    }

    public void validateDuplicateEmployee(Employee employee) {
        if (existsByEmpNum(employee.getEmpNum())) {
            throw new IllegalStateException("이미 존재하는 사용자입니다.");
        }
    }

    public Optional<Employee> findByEmployeeNum(Long empNum) {
        return employeeRepository.findByEmpNum(empNum);
    }

    public boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public LoginResponseDTO getAccessToken(Employee employee, String rawPassword) {
        if (isPasswordValid(rawPassword, employee.getEmpPw())) {
            String accessToken = tokenProvider.generateAccessToken(employee, Duration.ofMinutes(60));
            String refreshToken = tokenProvider.generateRefreshToken(employee, Duration.ofDays(7));

            redisTemplate.opsForValue().set("TOKEN:" + employee.getEmpNum(), accessToken, Duration.ofMinutes(60));
            redisTemplate.opsForValue().set("REFRESH_TOKEN:" + employee.getEmpNum(), refreshToken, Duration.ofDays(7));

            return new LoginResponseDTO(employee.getEmpNum(), employee.getEmpPw(), accessToken, refreshToken);
        }
        return null;
    }

    public void logout(Long empNum) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmpNum(empNum);
        if (employeeOptional.isPresent()) {
            redisTemplate.delete("TOKEN:" + empNum);
            redisTemplate.delete("REFRESH_TOKEN:" + empNum);
        } else {
            throw new UsernameNotFoundException("User not found with empNum: " + empNum);
        }
    }

    public LoginResponseDTO refreshAccessToken(Long empNum, String refreshToken) {
        String storedRefreshToken = (String) redisTemplate.opsForValue().get("REFRESH_TOKEN:" + empNum);
        if (storedRefreshToken != null && storedRefreshToken.equals(refreshToken)) {
            Optional<Employee> employeeOptional = employeeRepository.findByEmpNum(empNum);
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                String newAccessToken = tokenProvider.generateAccessToken(employee, Duration.ofMinutes(60));

                redisTemplate.opsForValue().set("TOKEN:" + empNum, newAccessToken, Duration.ofMinutes(60));
                redisTemplate.opsForValue().set("REFRESH_TOKEN:" + empNum, refreshToken, Duration.ofDays(7));

                return new LoginResponseDTO(employee.getEmpNum(), employee.getEmpPw(), newAccessToken, refreshToken);
            }
        }
        return null;
    }

    public boolean validateAccessToken(String token) {
        return tokenProvider.validateToken(token);
    }
}
