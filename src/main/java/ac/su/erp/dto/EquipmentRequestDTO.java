package ac.su.erp.dto;

import ac.su.erp.constant.ApprovalStatus;
import ac.su.erp.constant.DeleteStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EquipmentRequestDTO {
    private Long equipId;
    private String equipName;
    private String equipInfo;
    private Integer equipAmount;
    private Long equipCost;
    private Long equipSumcost;
    private String equipWhy;
    private LocalDate reqDate;
    private String kinds;
    private DeleteStatus del;
    private ApprovalStatus approvalStatus;
    private String meta;
    private String approver;  // 승인자 이름을 저장하는 필드

    // 기본값 설정 메서드 추가
    public void setDefaultValues() {
        if (this.reqDate == null) {
            this.reqDate = LocalDate.now();
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