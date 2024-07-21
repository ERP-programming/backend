package ac.su.erp.service;

import ac.su.erp.domain.Employee;
import ac.su.erp.domain.WorkTime;
import ac.su.erp.repository.WorkTimeRepository;
import org.springframework.stereotype.Service;

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
        workTime.setOnWork(LocalTime.now());
        workTime.setOffWork(null);
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
        workTime.setOffWork(LocalTime.now());
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
}
