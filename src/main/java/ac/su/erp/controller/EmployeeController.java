package ac.su.erp.controller;

import ac.su.erp.domain.Bank;
import ac.su.erp.domain.Department;
import ac.su.erp.domain.Employee;
import ac.su.erp.dto.EmployeeCreateForm;
import ac.su.erp.dto.LoginForm;
import ac.su.erp.repository.BankRepository;
import ac.su.erp.repository.DepartmentRepository;
import ac.su.erp.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;
    private final BankRepository bankRepository;


    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginForm", new LoginForm());
        return modelAndView;
    }

    @GetMapping("/Hr")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.findResignedEmployees();
        model.addAttribute("employees", employees);
        return "Hr";
    }

    @GetMapping("/search")
    public String searchEmployees(@RequestParam String empName, Model model) {
        List<Employee> employees = employeeService.findByEmployeeName(empName);
        model.addAttribute("employees", employees);
        return "Hr";  // 같은 뷰 템플릿으로 이동
    }



    @GetMapping("/create")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("employeeForm", new EmployeeCreateForm());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("banks", bankRepository.findAll());
        return "NewEmployForm";
    }

//    @PostMapping("/create")
//    public String createEmployee(@ModelAttribute EmployeeCreateForm form, Model model) {
//        try {
//            employeeService.createEmployee(form);
//            return "redirect:/Hr"; // 성공 시 hr 페이지로 리다이렉트
//        } catch (Exception e) {
//            model.addAttribute("error", "An error occurred while creating the employee.");
//            return "NewEmployForm"; // 오류 발생 시 같은 페이지 반환
//        }
//    }

    @PostMapping("/create")
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

    @GetMapping("update/{empNum}")
    public String showUpdateEmployeeForm(@PathVariable Long empNum, Model model) {
        Optional<Employee> employeeOptional = employeeService.findByEmployeeNum(empNum);
        model.addAttribute("employee", employeeOptional.get());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("banks", bankRepository.findAll());
        return "UpdateEmployeeForm";
    }

    @PostMapping("update/{empNum}")
    public String updateEmployee(@PathVariable Long empNum, @ModelAttribute EmployeeCreateForm form, Model model) {
        try {
            employeeService.updateEmployee(empNum, form);
            return "redirect:/Hr";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while updating the employee.");
            return "updateEmployee";
        }
    }


    //사원 퇴사 처리
    @PostMapping("/resign/{empNum}")
    public ModelAndView resignEmployee(@PathVariable Long empNum) {
        ModelAndView modelAndView = new ModelAndView("resignEmployee");
        try {
            employeeService.resignEmployee(empNum);
            modelAndView.setStatus(HttpStatus.OK);
            modelAndView.addObject("message", "사원 퇴사 처리에 성공했습니다.");
            modelAndView.setViewName("resignEmployeeSuccess");
            return modelAndView;
        } catch (UsernameNotFoundException e) {
            modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
            modelAndView.addObject("message", "유효하지 않은 사용자 ID입니다.");
            return modelAndView;
        } catch (Exception e) {
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            modelAndView.addObject("message", "사원 퇴사 처리에 실패했습니다. 오류: " + e.getMessage());
            return modelAndView;
        }
    }

    @PostMapping("/changePassword/{empNum}")
    public ModelAndView changePassword(@PathVariable Long empNum, @RequestBody String newPassword) {
        ModelAndView modelAndView = new ModelAndView("changePassword");
        try {
            employeeService.changePassword(empNum, newPassword);
            modelAndView.setStatus(HttpStatus.OK);
            modelAndView.addObject("message", "비밀번호 변경에 성공했습니다.");
            modelAndView.setViewName("changePasswordSuccess");
            return modelAndView;
        } catch (UsernameNotFoundException e) {
            modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
            modelAndView.addObject("message", "유효하지 않은 사용자 ID입니다.");
            return modelAndView;
        } catch (Exception e) {
            modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            modelAndView.addObject("message", "비밀번호 변경에 실패했습니다. 오류: " + e.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/{empNum}")
    public ModelAndView getEmployee(@PathVariable Long empNum) {
        ModelAndView modelAndView = new ModelAndView("employeeDetail");
        Optional<Employee> employeeOptional = employeeService.findByEmployeeNum(empNum);
        if (employeeOptional.isEmpty()) {
            modelAndView.setStatus(HttpStatus.NOT_FOUND);
            modelAndView.addObject("message", "사원 정보를 찾을 수 없습니다.");
            modelAndView.setViewName("employeeNotFound");
            return modelAndView;
        }
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.addObject("employee", employeeOptional.get());
        return modelAndView;
    }
}