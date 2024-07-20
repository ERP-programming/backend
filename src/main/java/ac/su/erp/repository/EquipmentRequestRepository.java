package ac.su.erp.repository;

import ac.su.erp.domain.EquipmentRequest;
import ac.su.erp.constant.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRequestRepository extends JpaRepository<EquipmentRequest, Long> {
    List<EquipmentRequest> findByApprovalStatus(ApprovalStatus approvalStatus);
}
