
package ac.su.erp.domain;

import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 휴직정보 엔티티
@Entity
@Table(name = "BREAK_TIME")
@Getter
@Setter
public class BreakTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BT_ID") // 휴직정보 ID
    private Long btId;

    @Column(name = "BT_START", nullable = false)    // 휴직 시작일
    private LocalDate btStart;

    @Column(name = "BT_END", nullable = false)  // 휴직 종료일
    private LocalDate btEnd;

    @Column(name = "BT_WHY", nullable = false)  // 휴직 사유
    private String btWhy;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    // N:1 매핑
    @ManyToOne
    @JoinColumn(name = "EMP_NUM")   // 사원번호
    private Employee employee;
}
