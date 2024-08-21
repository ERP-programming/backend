package ac.su.erp.service;

import ac.su.erp.domain.Employee;
import ac.su.erp.domain.WorkTime;
import ac.su.erp.dto.WorkTimeSummary;
import ac.su.erp.repository.WorkTimeRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class WorkTimeService {

    private final WorkTimeRepository workTimeRepository;

    public WorkTimeService(WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
    }

    // 특정 사원의 '모든' WorkTime 불러오기
    public List<WorkTime> getAllWorkTimes() {
        Employee employee = new Employee();
        employee.setEmpNum(1L); ////////// 1번 사원 가데이터 입력
        return workTimeRepository.findByEmployee(employee);
    }

    // 특정 사원의 오늘 WorkTime 불러오기
    public WorkTime getTodayWorkTime() {
        Employee employee = new Employee();
        employee.setEmpNum(1L); // 1번 사원 가데이터 입력
        LocalDate today = LocalDate.now(); // 오늘 날짜
        List<WorkTime> workTimes = workTimeRepository.findMostRecentWorkTime(employee, today);
        return workTimes.isEmpty() ? null : workTimes.get(0); // 오늘의 출근 기록이 없는 경우 null 반환
    }

    // 출근
    public void startWork() {
        Employee employee = new Employee();
        employee.setEmpNum(1L); // 1번 사원 가데이터 입력
        WorkTime workTime = new WorkTime();
        workTime.setEmployee(employee);
        workTime.setTodayDate(LocalDate.now());
        workTime.setOnWork(LocalTime.now().withNano(0)); // 밀리초 제거
//        workTime.setTodayDate(LocalDate.of(2024, 7, 22));
//        workTime.setOnWork(LocalTime.of(9, 0, 0));
        workTimeRepository.save(workTime);
    }

    // 퇴근
    public void endWork() {
        Employee employee = new Employee();
        employee.setEmpNum(1L); // 1번 사원 가데이터 입력
        LocalDate today = LocalDate.now();
        List<WorkTime> workTimes = workTimeRepository.findMostRecentWorkTime(employee, today);
        if (workTimes.isEmpty()) {
            throw new IllegalArgumentException("오늘의 출근 기록을 찾을 수 없습니다.");
        }
        WorkTime workTime = workTimes.get(0);
        LocalTime now = LocalTime.now().withNano(0); // 밀리초 제거ㅈ
        workTime.setOffWork(now);

        // 총 근무 시간 계산
        Duration totalWorkingDuration = Duration.between(workTime.getOnWork(), now);
        LocalTime totalWorkingTime = LocalTime.ofSecondOfDay(totalWorkingDuration.getSeconds());
        workTime.setTotalWorking(totalWorkingTime);

        // 초과 근무 시간 계산 (기준: 9시간)
        Duration nineHours = Duration.ofHours(9);
        Duration excessAllowDuration = totalWorkingDuration.minus(nineHours);
        LocalTime excessAllowTime = LocalTime.ofSecondOfDay(excessAllowDuration.isNegative() ? 0 : excessAllowDuration.getSeconds());
        workTime.setExcessAllow(excessAllowTime);

        workTimeRepository.save(workTime);
    }

    // 출근 여부 확인
    public boolean isStartWork() {
        Employee employee = new Employee();
        employee.setEmpNum(1L); // 1번 사원 가데이터 입력
        LocalDate today = LocalDate.now();
        List<WorkTime> workTimes = workTimeRepository.findMostRecentWorkTime(employee, today);
        return !workTimes.isEmpty() && workTimes.get(0).getOffWork() == null;
    }

    //
    // 특정 사원의 일주일 기록 조회
    public List<WorkTime> getWeeklyWorkTimes() {
        Employee employee = new Employee();
        employee.setEmpNum(1L); // 1번 사원 가데이터 입력
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(DayOfWeek.SUNDAY);
        return workTimeRepository.findByEmployeeAndTodayDateBetween(employee, monday, sunday);
    }

    // 일주일 기록의 총 근무시간과 초과시간 계산
    public WorkTimeSummary calculateWeeklySummary() {
        List<WorkTime> weeklyWorkTimes = getWeeklyWorkTimes();

        Duration totalWorkDuration = Duration.ZERO;
        Duration totalExcessAllow = Duration.ZERO;

        for (WorkTime workTime : weeklyWorkTimes) {
            LocalTime onWorkTime = workTime.getOnWork();
            LocalTime offWorkTime = workTime.getOffWork();
            if (onWorkTime != null && offWorkTime != null) {
                Duration dailyDuration = Duration.between(onWorkTime, offWorkTime);
                totalWorkDuration = totalWorkDuration.plus(dailyDuration);

                // Directly use the `excessAllow` column value for excess hours
                Duration dailyExcessAllow = Duration.ofHours(workTime.getExcessAllow().getHour())
                        .plusMinutes(workTime.getExcessAllow().getMinute());
                totalExcessAllow = totalExcessAllow.plus(dailyExcessAllow);
            }
        }

        WorkTimeSummary summary = new WorkTimeSummary();
        summary.setTotalWorkHours(totalWorkDuration.toHours());
        summary.setExcessHours(totalExcessAllow.toHours());

        return summary;
    }
}
