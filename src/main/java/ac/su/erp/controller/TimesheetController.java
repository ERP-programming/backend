package ac.su.erp.controller;

import ac.su.erp.domain.WorkTime;
import ac.su.erp.dto.WorkTimeSummary;
import ac.su.erp.service.WorkTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timesheet")
public class TimesheetController {

    private final WorkTimeService workTimeService;

    @GetMapping
    public String workPage(Model model) {

        List<WorkTime> workTimes = workTimeService.getWeeklyWorkTimes();
        model.addAttribute("workTimes", workTimes);

        WorkTimeSummary workTimeSummary = workTimeService.calculateWeeklySummary();
        model.addAttribute("workTimeSummary", workTimeSummary);

        return "timesheet";
    }
}
