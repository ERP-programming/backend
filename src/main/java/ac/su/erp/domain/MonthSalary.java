package ac.su.erp.domain;

import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

// 월급 엔티티
@Entity
@Table(name = "MONTH_SALARY")
@Getter
@Setter
public class MonthSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MONTH_SALARY_ID")    // 월급 ID
    private Long monthSalaryId;

    @Column(name = "SALARY", nullable = false)  // 기본급
    private Long salary;

    @Column(name = "OVERTIME_PAY", nullable = false)    // 초과수당
    private Long overtimePay = 0L;

    @Column(name = "INSURANCE", nullable = false)   // 4대보험
    private Long insurance = 0L;

    @Column(name = "NATIONAL_PENSION", nullable = false)   // 국민연금
    private Long nationalPension = 0L;

    @Column(name = "HEALTH", nullable = false)  // 건강보험
    private Long health = 0L;

    @Column(name = "CARE_INSURANCE", nullable = false)  // 요양보험
    private Long careInsurance = 0L;

    @Column(name = "FOOD_EXPENSES", nullable = false)  // 식대
    private Long foodExpenses;

    @Column(name = "REAL_PAY", nullable = false)    // 실급여
    private Long realPay;

    @Column(name = "PAY_DATE", nullable = false)    // 지급일
    private Date payDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    // N:1 매핑
    @ManyToOne
    @JoinColumn(name = "EMP_NUM")   // 사원번호
    private Employee employee;
}
