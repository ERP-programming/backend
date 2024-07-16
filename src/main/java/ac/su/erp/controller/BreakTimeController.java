package ac.su.erp.controller;

import ac.su.erp.dto.BreakTimeDTO;
import ac.su.erp.service.BreakTimeService;
import ac.su.erp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/break-times")
public class BreakTimeController {

    @Autowired
    private BreakTimeService breakTimeService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{id}")
    public String getBreakTimeById(@PathVariable Long id, Model model) {
        BreakTimeDTO breakTimeDTO = breakTimeService.getBreakTimeById(id);
        model.addAttribute("breakTime", breakTimeDTO);
        return "breakTimeDetail";
    }

    @GetMapping
    public String getAllBreakTimes(Model model) {
        List<BreakTimeDTO> breakTimes = breakTimeService.getAllBreakTimes();
        model.addAttribute("breakTimes", breakTimes);
        return "breakTimeList";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("breakTime", new BreakTimeDTO());
        return "breakTimeForm";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        BreakTimeDTO breakTimeDTO = breakTimeService.getBreakTimeById(id);
        model.addAttribute("breakTime", breakTimeDTO);
        return "breakTimeForm";
    }

    @PostMapping
    public String createBreakTime(@ModelAttribute BreakTimeDTO breakTimeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자 인증이 되지 않은 경우, 에러 페이지 또는 로그인 페이지로 리다이렉션
            return "redirect:/login"; // 로그인 페이지 경로로 변경
        }
        String empNumStr = authentication.getName();
        Long empNum = Long.parseLong(empNumStr);
        Long employeeId = employeeService.findByEmpNum(empNum).getEmpNum();
        breakTimeDTO.setEmployeeId(employeeId);
        breakTimeService.createBreakTime(breakTimeDTO);
        return "redirect:/break-times";
    }

    @PutMapping("/{id}")
    public String updateBreakTime(@PathVariable Long id, @ModelAttribute BreakTimeDTO breakTimeDTO) {
        breakTimeService.updateBreakTime(id, breakTimeDTO);
        return "redirect:/break-times";
    }

    @DeleteMapping("/{id}")
    public String deleteBreakTime(@PathVariable Long id) {
        breakTimeService.deleteBreakTime(id);
        return "redirect:/break-times";
    }
}
