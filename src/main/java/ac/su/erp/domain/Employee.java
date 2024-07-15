package ac.su.erp.domain;

import ac.su.erp.constant.DepartmentPosition;
import ac.su.erp.constant.EmployeePosition;
import ac.su.erp.constant.EmploymentStatus;
import ac.su.erp.constant.GenderEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 사원정보 엔티티
@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
public class Employee {
    @Id
    @Column(name = "EMP_NUM")   // 사원번호
    private Long empNum;

    @Column(name = "EMP_AGE", nullable = false)  // 나이
    private Long empBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMP_DEL", nullable = false) // 퇴사여부
    private EmploymentStatus empDel = EmploymentStatus.EMPLOYED;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMP_HEAD", nullable = false)    // 부서원 or 부서장
    private DepartmentPosition empHead;

    @Column(name = "EMP_NAME", nullable = false)    // 이름
    private String empName;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMP_GENDER", nullable = false)  // 성별
    private GenderEnum empGender;

    @Column(name = "EMP_INTRODUCE") // 사원정보(소개)
    private String empIntroduce;

    @Column(name = "EMP_BIRTHNUM", nullable = false)    // 주민번호
    private String empBirthNum;

    @Column(name = "EMP_PNUM", nullable = false)    // 연락처
    private String empPnum;

    @Column(name = "EMP_ADDR", nullable = false)    // 주소
    private String empAddr;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMP_POS", nullable = false) // 직급
    private EmployeePosition empPos;

    @Column(name = "EMP_PW", nullable = false)  // 비밀번호
    private String empPw;

    @Column(name = "EMP_EMAIL", nullable = false)   // 이메일
    private String empEmail;

    @Column(name = "START_DAY", nullable = false)   // 입사일
    private LocalDate startDay;

    @Column(name = "END_DAY")   // 퇴사일
    private LocalDate endDay;

    @Column(name = "EMP_INFO_CHANGE")   // 사원정보 변경일
    private LocalDateTime empInfoChange = LocalDateTime.now();  // 기본값은 현재 날짜

    @Column(name = "EMP_DELINFO")   // 퇴사정보(사유)
    private String empDelInfo;

    @Column(name = "EMP_BANKNUM", nullable = false)   // 계좌번호
    private String empBanknum;

    // N:1 매핑
    @ManyToOne
    @JoinColumn(name = "bank_code") // 은행코드
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "dept_no")   // 부서번호
    private Department department;

    // 1:N 매핑

    @OneToMany(mappedBy = "employee")  // 출퇴근시간 참조
    private List<WorkTime> workTimes;

    @OneToMany(mappedBy = "employee")   // 월급 참조
    private List<MonthSalary> monthSalaries;

    @OneToMany(mappedBy = "employee")  // 공지사항 참조
    private List<Notice> notices;

    @OneToMany(mappedBy = "employee")   // 휴직정보 참조
    private List<BreakTime> breakTimes;

    @OneToMany(mappedBy = "employee")   // 할일 참조
    private List<Task> tasks;

    @OneToMany(mappedBy = "sender")  // AnnualRequest의 sender 필드 참조
    private List<AnnualRequest> sentAnnualRequests;

    @OneToMany(mappedBy = "approver")  // AnnualRequest의 approver 필드 참조
    private List<AnnualRequest> approvedAnnualRequests;

    @OneToMany(mappedBy = "sender")  // AnnualRequest의 sender 필드 참조
    private List<EquipmentRequest> sentEquipmentRequests;

    @OneToMany(mappedBy = "approver")  // AnnualRequest의 approver 필드 참조
    private List<EquipmentRequest> approvedEquipmentRequests;

    // 1:1 매핑
    @OneToOne(mappedBy = "employee")
    private Contract contract;
}