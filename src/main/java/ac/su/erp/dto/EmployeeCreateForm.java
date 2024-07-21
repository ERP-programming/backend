package ac.su.erp.dto;

import ac.su.erp.constant.DepartmentPosition;
import ac.su.erp.constant.EmployeePosition;
import ac.su.erp.constant.GenderEnum;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class EmployeeCreateForm {
    private String empName;
    private Long empNum;
    private String empBirthNum;
    private Long empBirth;
    private String empPnum;
    private String empEmail;
    private String empAddr;
    private GenderEnum empGender;
    private String empBanknum;
    private Long salary;
    private String bankCode;
    private Long deptNo;
    private LocalDate contractStart;
    private LocalDate contractEnd;
}

