package ac.su.erp.dto;

import ac.su.erp.constant.DepartmentPosition;
import ac.su.erp.constant.EmployeePosition;
import ac.su.erp.constant.GenderEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EmployeeCreateForm {
    @NotNull
    private Long empNum;

    @NotNull
    private Long empBirth;

    @NotNull
    private DepartmentPosition empHead;

    @NotNull
    private String empName;

    @NotNull
    private GenderEnum empGender;

    private String empIntroduce;

    @NotNull
    private String empBirthNum;

    @NotNull
    private String empPnum;

    @NotNull
    private String empAddr;

    @NotNull
    private EmployeePosition empPos;

    @NotNull
    private String empPw;

    @NotNull
    private String empEmail;

    @NotNull
    private Date startDay;

    private String empDelInfo;

    @NotNull
    private String empBanknum;

    @NotNull
    private String bankCode;

    @NotNull
    private Long deptNo;
}
