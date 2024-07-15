package ac.su.erp.domain;

import ac.su.erp.constant.ApprovalStatus;
import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 연차신청 엔티티
@Entity
@Table(name = "ANNUAL_REQUEST")
@Getter
@Setter
public class AnnualRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")    // 연차신청 ID
    private Long id;

    @Column(name = "EXPECTED_DATE")   // 연차 예정일
    private LocalDate expectedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVAL", nullable = false)    // 승인여부
    private ApprovalStatus approval = ApprovalStatus.PENDING;   // 기본값은 PENDING

    @Column(name = "REQ_DATE", nullable = false)    // 신청일
    private LocalDateTime reqDate = LocalDateTime.now();   // 기본값은 현재 날짜

    @Column(name = "KINDS", nullable = false)   // 신청 종류
    private String kinds = "연차";    // 기본값은 "연차"

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    // N:1 매핑
    @ManyToOne
    @JoinColumn(name = "SENDER_EMP_NUM", referencedColumnName = "EMP_NUM")     // 요청자
    private Employee sender;

    @ManyToOne
    @JoinColumn(name = "APPROVER_EMP_NUM", referencedColumnName = "EMP_NUM")   // 승인자
    private Employee approver;
}

