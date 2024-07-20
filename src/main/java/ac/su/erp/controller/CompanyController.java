package ac.su.erp.controller;

import ac.su.erp.domain.Company;
import ac.su.erp.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{comNum}")
    public ResponseEntity<?> getCompany(@PathVariable String comNum) {
        Optional<Company> companyOptional = companyService.findCompanyByComNum(comNum);
        if (companyOptional.isPresent()) {
            return ResponseEntity.ok(companyOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        }
    }
}