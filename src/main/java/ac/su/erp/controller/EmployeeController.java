package ac.su.erp.controller;

import ac.su.erp.domain.Employee;
import ac.su.erp.dto.EmployeeCreateForm;
import ac.su.erp.dto.LoginForm;
import ac.su.erp.repository.BankRepository;
import ac.su.erp.repository.DepartmentRepository;
import ac.su.erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BankRepository bankRepository;
    private final DepartmentRepository departmentRepository;

    @GetMapping()
    public ModelAndView getAllEmployees() {
        ModelAndView modelAndView = new ModelAndView("employees");
        modelAndView.addObject("employees", employeeService.findAllEmployees());
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginForm", new LoginForm());
        return modelAndView;
    }

    @GetMapping("/create")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("employeeCreateForm", new EmployeeCreateForm());
        return "employee-create";
    }

    @PostMapping("/create")
    public String createEmployee(@ModelAttribute EmployeeCreateForm form, Model model) {
        try {
            employeeService.createEmployee(form);
            model.addAttribute("successMessage", "Employee created successfully");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating employee: " + e.getMessage());
        }
        return "employee-create";
    }

//    @PostMapping()
//    public String saveEmployee(@ModelAttribute Employee employee) {
//        employeeService.createEmployee(employee);
//        return "redirect:/employees";
//    }

    //사원 퇴사(삭제) 처리
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

    @PostMapping("/logout")
    public ModelAndView logout(@RequestBody Map<String, String> request) {
        ModelAndView modelAndView = new ModelAndView("logout");
        Long empNum = Long.valueOf(request.get("empNum"));
            employeeService.logout(empNum);
            modelAndView.setStatus(HttpStatus.OK);
            modelAndView.addObject("message", "로그아웃에 성공했습니다.");
            modelAndView.setViewName("logoutSuccess");
            return modelAndView;

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