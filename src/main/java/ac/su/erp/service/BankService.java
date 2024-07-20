package ac.su.erp.service;

import ac.su.erp.domain.Bank;
import ac.su.erp.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;

}
