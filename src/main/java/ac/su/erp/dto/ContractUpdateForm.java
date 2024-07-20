package ac.su.erp.dto;

import ac.su.erp.constant.DepartmentPosition;
import ac.su.erp.constant.EmployeePosition;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ContractUpdateForm {
    private Long empNum;
    private String empName;
    private String empBanknum;
    private String bankCode;
    private Long salary;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractEnd;
}
