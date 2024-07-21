package ac.su.erp.repository;

import ac.su.erp.domain.Employee;
import ac.su.erp.domain.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long> {
    List<WorkTime> findByEmployee(Employee employee);

    // 특정 사원의 오늘 WorkTime 중 가장 최근의 WorkTime을 조회하는 JPQL 쿼리
    @Query("SELECT wt FROM WorkTime wt WHERE wt.employee = :employee AND wt.todayDate = :today ORDER BY wt.onWork DESC")
    List<WorkTime> findMostRecentWorkTime(Employee employee, LocalDate today);

    List<WorkTime> findByEmployeeAndTodayDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);
}
