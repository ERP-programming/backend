package ac.su.erp.domain;

import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 출퇴근시간 엔티티
@Entity
@Table(name = "WORK_TIME")
@Getter
@Setter
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_TIME_ID")    // 출퇴근시간 ID
    private Long workTimeId;

    @Column(name = "TODAY_DATE", nullable = false)  // 오늘 날짜
    private LocalDate todayDate = LocalDate.now();  // 기본값은 현재 날짜

    @Column(name = "ON_WORK", nullable = false)    // 출근시간
    private LocalDate onWork = LocalDate.now();  // 기본값은 현재 날짜

    @Column(name = "OFF_WORK", nullable = false)    // 퇴근시간
    private LocalDate offWork = LocalDate.now();  // 기본값은 현재 날짜

    @Column(name = "TOTAL_WORKING", nullable = false)   // 총 근무시간
    private String totalWorking = "0";

    @Column(name = "EXCESS_ALLOW", nullable = false)    // 초과시간
    private String excessAllow = "0";

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    // N:1 매핑
    @ManyToOne
    @JoinColumn(name = "EMP_NUM")   // 사원번호
    private Employee employee;
}
