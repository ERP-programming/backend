package ac.su.erp.repository;

import ac.su.erp.domain.BreakTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreakTimeRepository extends JpaRepository<BreakTime, Long> {
}
