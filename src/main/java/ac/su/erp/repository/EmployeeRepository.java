package ac.su.erp.repository;

import ac.su.erp.constant.EmploymentStatus;
import ac.su.erp.domain.Department;
import ac.su.erp.domain.Employee;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmpName(String empName);
    Optional<Employee> findByEmpNum(Long empNum);
    boolean existsByEmpNum(Long empNum);
    List<Employee> findByEmpDel(EmploymentStatus status);

}
