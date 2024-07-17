package ac.su.erp.dto;

import ac.su.erp.constant.DepartmentPosition;
import ac.su.erp.constant.EmployeePosition;
import ac.su.erp.constant.EmploymentStatus;
import ac.su.erp.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileDTO {
    private Long empNum;
    private Long empBirth;
    private EmploymentStatus empDel = EmploymentStatus.EMPLOYED;
    private DepartmentPosition empHead = DepartmentPosition.DepartmentMember;
    private String empName;
    private GenderEnum empGender = GenderEnum.MALE;
    private String empIntroduce;
    private String empBirthNum;
    private String empPnum;
    private String empAddr;
    private EmployeePosition empPos;
    private String empPw;
    private String empEmail;
    private LocalDate startDay;
    private LocalDate endDay;
    private String empDelInfo;
    private String empBanknum;
}
