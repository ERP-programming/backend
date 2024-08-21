package ac.su.erp.service;

import ac.su.erp.domain.Company;
import ac.su.erp.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Optional<Company> findCompanyByComNum(String comNum) {
        return companyRepository.findById(comNum);
    }
}
