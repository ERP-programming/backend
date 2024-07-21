package ac.su.erp.domain;

import ac.su.erp.constant.ApprovalStatus;
import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 비품 요청 엔티티
@Entity
@Table(name = "EQUIPMENT_REQUEST")
@Getter
@Setter
public class EquipmentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EQUIP_ID")  // 비품 요청 번호
    private Long equipId;

    @Column(name = "EQUIP_NAME", nullable = false)  // 비품명
    private String equipName;

    @Column(name = "EQUIP_INFO")    // 비품 정보
    private String equipInfo;

    @Column(name = "EQUIP_AMOUNT")  // 비품 수량
    private Integer equipAmount;

    @Column(name = "EQUIP_COST")    // 비품 단가
    private Long equipCost;

    @Column(name = "EQUIP_SUMCOST") // 비품 총액
    private Long equipSumcost;

    @Column(name = "EQUIP_WHY") // 비품 요청 사유
    private String equipWhy;

    @Column(name = "REQ_DATE", nullable = false)    // 요청 일자
    private LocalDateTime reqDate = LocalDateTime.now();   // 기본값은 현재 일자

    @Column(name = "KINDS", nullable = false)   // 신청 종류
    private String kinds = "비품";    // 기본값은 "비품"

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVAL", nullable = false)    // 승인상태
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;   // 기본값은 PENDING

    // N:1 매핑
    @ManyToOne
    @JoinColumn(name = "SENDER_EMP_NUM")    // 요청자
    private Employee sender;

    @ManyToOne
    @JoinColumn(name = "APPROVER_EMP_NUM")  // 승인자
    private Employee approver;

    public void setDefaultValues() {
        if (this.reqDate == null) {
            this.reqDate = LocalDateTime.now();
        }
        if (this.kinds == null) {
            this.kinds = "비품";
        }
        if (this.del == null) {
            this.del = DeleteStatus.ACTIVE;
        }
        if (this.approvalStatus == null) {
            this.approvalStatus = ApprovalStatus.PENDING;
        }
    }
}
