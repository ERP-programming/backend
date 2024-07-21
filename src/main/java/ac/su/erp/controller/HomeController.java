package ac.su.erp.controller;

import ac.su.erp.domain.Notice;
import ac.su.erp.domain.Task;
import ac.su.erp.domain.WorkTime;
import ac.su.erp.service.TaskService;
import ac.su.erp.service.NoticeService;
import ac.su.erp.service.WorkTimeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final NoticeService noticeService;
    private final TaskService taskService;
    private final WorkTimeService workTimeService;

    public HomeController(NoticeService noticeService, TaskService taskService, WorkTimeService workTimeService) {
        this.noticeService = noticeService;
        this.taskService = taskService;
        this.workTimeService = workTimeService;
    }

    @GetMapping
    public String home(Model model) {

        List<Notice> notices = noticeService.getAllNotices();
        model.addAttribute("notices", notices);

        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);

        WorkTime todayWork = workTimeService.getTodayWorkTime();
        model.addAttribute("todayWork", todayWork);

        return "home";
    }

}
