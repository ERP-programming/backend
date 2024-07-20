package ac.su.erp.repository;

import ac.su.erp.domain.Employee;
import ac.su.erp.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEmployee(Employee employee);
}