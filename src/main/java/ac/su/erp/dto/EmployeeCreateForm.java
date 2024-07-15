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
    private Long deptNo;
    private String salary;
    private String contractStart;
    private String contractEnd;
    private String accountNum;  // 계좌번호 추가
}
