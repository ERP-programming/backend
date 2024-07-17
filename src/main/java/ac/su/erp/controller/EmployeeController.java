package ac.su.erp.controller;

import ac.su.erp.dto.ProfileDTO;
import ac.su.erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/profileModalContent")
    public String getProfileModalContent(@RequestParam("id") Long employeeId, Model model) {
        ProfileDTO employee = employeeService.getProfileDtoById(employeeId);
        model.addAttribute("employee", employee);
        return "modals/ProfileModalContent";
    }
}
