package ac.su.erp.dto;

import lombok.Data;

@Data
public class EmployeeCreateForm {
    private Long empNum;
    private String id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Long bankCode;
    private Long deptNo; // 부서번호 추가
    private String salary;
    private String contractStart;
    private String contractEnd;
}
