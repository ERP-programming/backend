package ac.su.erp.controller;

import ac.su.erp.domain.Employee;
import ac.su.erp.dto.EmployeeCreateForm;
import ac.su.erp.dto.LoginDTO;
import ac.su.erp.dto.LoginResponseDTO;
import ac.su.erp.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(
            @Validated @RequestBody EmployeeCreateForm employeeCreateForm,
            BindingResult bindingResult
    ) {
        // 1. Form 데이터 검증
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력값에 오류가 있습니다.");
        }

        // 2. 백엔드 validation
        try {
            employeeService.createEmployee(employeeCreateForm);
        } catch (IllegalStateException e) {
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 등록된 사용자입니다.");
        } catch (Exception e) {
            bindingResult.reject("signupFailed", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        // 3. 회원 가입 성공
        return ResponseEntity.status(HttpStatus.OK).body("회원 가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @Validated @RequestBody LoginDTO loginDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 자격 증명입니다.");
        }

        Optional<Employee> employeeOptional = employeeService.findByEmployeeNum(loginDTO.getEmpNum());
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자 ID입니다.");
        }

        Employee employee = employeeOptional.get();
        if (!employeeService.isPasswordValid(loginDTO.getPassword(), employee.getEmpPw())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자 비밀번호입니다.");
        }

        try {
            LoginResponseDTO loginResponse = employeeService.getAccessToken(employee, loginDTO.getPassword());
            if (loginResponse == null) {
                throw new Exception("토큰 생성 실패");
            }
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 생성에 실패했습니다. 오류: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        Long empNum = Long.valueOf(request.get("empNum"));
        try {
            employeeService.logout(empNum);
            return ResponseEntity.status(HttpStatus.OK).body("로그아웃에 성공했습니다.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자 ID입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃에 실패했습니다. 오류: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        Long empNum = Long.valueOf(request.get("empNum"));
        String refreshToken = request.get("refreshToken");
        try {
            LoginResponseDTO loginResponse = employeeService.refreshAccessToken(empNum, refreshToken);
            if (loginResponse == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Refresh Token입니다.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 갱신에 실패했습니다. 오류: " + e.getMessage());
        }
    }

    @GetMapping("/protected-resource")
    public ResponseEntity<?> getProtectedResource(@RequestHeader(value = "Authorization", required = false) String authorization, HttpServletRequest request, HttpServletResponse response) {
        String token = Optional.ofNullable(authorization).map(auth -> auth.replace("Bearer ", "")).orElse("");
        boolean isTokenValid = employeeService.validateAccessToken(token);

        if (isTokenValid) {
            return ResponseEntity.status(HttpStatus.OK).body("Protected resource accessed successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유효하지 않은 토큰입니다.");
        }
    }

    //비밀번호 변경
    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request) {
        Long empNum = Long.valueOf(request.get("empNum"));
        String newPassword = request.get("newPassword");
        try {
            employeeService.changePassword(empNum, newPassword);
            return ResponseEntity.status(HttpStatus.OK).body("비밀번호 변경에 성공했습니다.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자 ID입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경에 실패했습니다. 오류: " + e.getMessage());
        }
    }
}
