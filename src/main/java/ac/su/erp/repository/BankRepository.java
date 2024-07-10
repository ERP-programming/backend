package ac.su.erp.repository;

import ac.su.erp.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findByBankCode(String bankCode);
}
