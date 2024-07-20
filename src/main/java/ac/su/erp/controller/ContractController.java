package ac.su.erp.controller;

import ac.su.erp.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/contracts")
public class ContractController {
    private final ContractService contractService;

    //계약 정보 변경
    @GetMapping("/update")
    public String updateContract() {
        return "update_contract";
    }

    //계약 정보 변경

}
