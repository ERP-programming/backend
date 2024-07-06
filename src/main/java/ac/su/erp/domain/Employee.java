package ac.su.erp.domain;

import ac.su.erp.constant.DepartmentPosition;
import ac.su.erp.constant.EmployeePosition;
import ac.su.erp.constant.EmploymentStatus;
import ac.su.erp.constant.GenderEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
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
    private Date startDay;

    @Column(name = "END_DAY")   // 퇴사일
    private Date endDay;

    @Column(name = "EMP_INFO_CHANGE")   // 사원정보 변경일
    private LocalDate empInfoChange = LocalDate.now();  // 기본값은 현재 날짜

    @Column(name = "EMP_DELINFO")   // 퇴사정보(사유)
    private String empDelInfo;

    @Column(name = "EMP_BANKNUM", nullable = false)   // 계좌번호
    private String empBanknum;

    // N:1 매핑
    @ManyToOne
    @JoinColumn(name = "bank_code") // 은행코드
    private Bank bankCode;

    @ManyToOne
    @JoinColumn(name = "dept_no")   // 부서번호
    private Department deptNo;

    // 1:N 매핑
    @OneToMany(mappedBy = "equipId")  // 비품 요청 번호
    private List<EquipmentRequest> equipmentRequests;

    @OneToMany(mappedBy = "workTimeId")  // 출퇴근시간 ID
    private List<WorkTime> workTimes;

    @OneToMany(mappedBy = "monthSalaryId")   // 월급 ID
    private List<MonthSalary> monthSalaries;

    @OneToMany(mappedBy = "noticeId")  // 공지사항 ID
    private List<Notice> notices;

    @OneToMany(mappedBy = "btId")   // 휴직정보 ID
    private List<BreakTime> breakTimes;

    // 1:1 매핑
    @OneToOne(mappedBy = "employee")
    private Contract contract;
}