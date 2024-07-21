package ac.su.erp.controller;

import ac.su.erp.domain.WorkTime;
import ac.su.erp.dto.WorkStatus;
import ac.su.erp.service.WorkTimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/workTimes")
public class WorkTimeController {

    private final WorkTimeService workTimeService;

    public WorkTimeController(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<WorkTime>> getAllWorkTimes() {
        List<WorkTime> workTimes = workTimeService.getAllWorkTimes();
        return ResponseEntity.ok(workTimes);
    }

    // 특정 사원의 오늘 WorkTime 가져오기
    @GetMapping("/today")
    public ResponseEntity<WorkTime> getTodayWorkTime() {
        WorkTime workTime = workTimeService.getTodayWorkTime();
        return ResponseEntity.ok(workTime);
    }

    // 출근 시작
    @PostMapping("/startWork")
    public ResponseEntity<Void> startWork() {
        workTimeService.startWork();
        return ResponseEntity.ok().build();
    }

    // 퇴근
    @PostMapping("/endWork")
    public ResponseEntity<Void> endWork() {
        workTimeService.endWork();
        return ResponseEntity.ok().build();
    }

    // 출근 여부 확인
    @GetMapping("/getWorkStatus")
    public ResponseEntity<WorkStatus> getWorkStatus() {
        boolean isStartWork = workTimeService.isStartWork();
        WorkStatus status = new WorkStatus();
        status.setStartWork(isStartWork);
        return ResponseEntity.ok(status);
    }

}
