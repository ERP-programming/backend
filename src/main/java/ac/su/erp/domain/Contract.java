package ac.su.erp.domain;

import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 계약(연봉) 엔티티
@Entity
@Table(name = "CONTRACT")
@Getter
@Setter
public class Contract {
    @Id
    @OneToOne
    @JoinColumn(name = "EMP_NUM")   // 사원번호
    private Employee employee;

    @Column(name = "CON_INCOME", nullable = false)  // 계약금액(연봉)
    private Long conIncome;

    @Column(name = "CON_STARTDAY", nullable = false)    // 계약 시작일
    private LocalDate conStartday;

    @Column(name = "CON_ENDDAY", nullable = false)  // 계약 종료일
    private LocalDate conEndday;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false)   // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE
}