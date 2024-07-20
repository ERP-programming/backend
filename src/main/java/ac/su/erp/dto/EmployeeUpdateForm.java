package ac.su.erp.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class EmployeeUpdateForm {
    private Long empNum;
    private String empName;
    private Long deptNo;
    private String empGender;

}
