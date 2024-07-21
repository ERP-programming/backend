package ac.su.erp.controller;

import ac.su.erp.domain.Employee;
import ac.su.erp.dto.*;
import ac.su.erp.repository.BankRepository;
import ac.su.erp.repository.DepartmentRepository;
import ac.su.erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;
    private final BankRepository bankRepository;

    // 로그인 폼 보여주기
    @GetMapping("/login")
    public ModelAndView showLoginForm() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginForm", new LoginForm());
        return modelAndView;
    }

    // 인사 정보 페이지
    @GetMapping("/Hr")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.findEmployedEmployees();
        model.addAttribute("employees", employees);
        return "Hr";
    }

    // 인사 정보 변경 시 직원 목록을 보는 페이지
    @GetMapping("/HrList")
    public String HrListEmployees(Model model) {
        List<Employee> employees = employeeService.findEmployedEmployees();
        model.addAttribute("employees", employees);
        return "EmployeeListHr";
    }

    // 계약 정보 변경 시 직원 목록을 보는 페이지
    @GetMapping("/ContractList")
    public String ContractListEmployees(Model model) {
        List<Employee> employees = employeeService.findEmployedEmployees();
        model.addAttribute("employees", employees);
        return "EmployeeListContract";
    }

    // 직원 생성 폼 보여주기
    @GetMapping("/create")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("employeeCreateForm", new EmployeeCreateForm());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("banks", bankRepository.findAll());
        return "NewEmployForm";
    }

    // 직원 생성
    @PostMapping("/create")
    public String createEmployee(@ModelAttribute EmployeeCreateForm form) {
        employeeService.createEmployee(form);
        return "redirect:/employees/Hr"; // 성공 시 HR 페이지로 리다이렉트
    }

    // 인사 정보 변경 폼 보여주기
    @GetMapping("/update/{empNum}")
    public String showUpdateEmployeeForm(@PathVariable Long empNum, Model model) {
        Optional<Employee> employeeOptional = employeeService.findByEmployeeNum(empNum);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            EmployeeUpdateForm form = new EmployeeUpdateForm();
            form.setEmpNum(employee.getEmpNum());
            form.setEmpName(employee.getEmpName());
            form.setDeptNo(employee.getDepartment().getDeptNo());
            form.setEmpPos(employee.getEmpPos());
            form.setEmpHead(employee.getEmpHead());

            model.addAttribute("employeeUpdateForm", form);
            model.addAttribute("departments", departmentRepository.findAll());
            return "UpdateEmployeeForm";
        } else {
            // 사원이 존재하지 않을 경우 처리
            return "redirect:/error"; // 또는 에러 페이지로 리디렉션
        }
    }

    // 인사 정보 변경
    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute("employeeUpdateForm") EmployeeUpdateForm form) {
        employeeService.updateEmployee(form);
        return "redirect:/employees/Hr"; // 성공 시 HR 페이지로 리다이렉트
    }

    // 계약 정보 변경 폼 보여주기
    @GetMapping("/updateContract/{empNum}")
    public String showUpdateContractForm(@PathVariable Long empNum, Model model) {
        Optional<Employee> employeeOptional = employeeService.findByEmployeeNum(empNum);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            ContractUpdateForm form = new ContractUpdateForm();
            form.setEmpNum(employee.getEmpNum());
            form.setEmpName(employee.getEmpName());
            form.setEmpBanknum(employee.getEmpBanknum());
            form.setBankCode(employee.getBank().getBankCode());
            form.setSalary(employee.getConIncome());
            form.setContractStart(employee.getConStartday());
            form.setContractEnd(employee.getConEndday());

            model.addAttribute("ContractUpdateForm", form);
            model.addAttribute("banks", bankRepository.findAll());
            return "UpdateContractForm";
        } else {
            // 사원이 존재하지 않을 경우 처리
            return "redirect:/error"; // 또는 에러 페이지로 리디렉션
        }
    }

    // 계약 정보 변경
    @PostMapping("/updateContract")
    public String updateContract(@ModelAttribute("ContractUpdateForm") ContractUpdateForm form) {
        employeeService.updateContract(form);
        return "redirect:/employees/Hr"; // 성공 시 HR 페이지로 리다이렉트
    }

    // 사원 퇴사 처리
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

    // 특정 사원 정보 조회
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

    // 프로필 모달 내용 가져오기
    @GetMapping("/profileModalContent")
    public String getProfileModalContent(@RequestParam("id") Long employeeId, Model model) {
        ProfileDTO employee = employeeService.getProfileDtoById(employeeId);
        model.addAttribute("employee", employee);
        return "modals/ProfileModalContent";
    }

    // 프로필 업데이트
    @PutMapping("/updateProfile")
    @ResponseBody
    public ResponseEntity<String> updateProfile(@RequestParam("id") Long employeeId, @RequestBody ProfileDTO profileDTO) {
        employeeService.updateEmployeeProfile(employeeId, profileDTO);
        return ResponseEntity.ok("success");
    }

    // 부서가 HR인지 확인하는 API
    @GetMapping("/hr-check")
    @ResponseBody
    public ResponseEntity<String> checkHrAccess() {
        String userDept = employeeService.getCurrentUserDepartment();

        if ("HR".equals(userDept)) {
            return ResponseEntity.ok("access-granted");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access-denied");
        }
    }
}
