package ac.su.erp.service;

import ac.su.erp.domain.EquipmentRequest;
import ac.su.erp.dto.EquipmentRequestDTO;
import ac.su.erp.repository.EquipmentRequestRepository;
import ac.su.erp.constant.ApprovalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentRequestService {

    @Autowired
    private EquipmentRequestRepository equipmentRequestRepository;

    public List<EquipmentRequestDTO> getAllRequests() {
        return equipmentRequestRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EquipmentRequestDTO getRequestById(Long id) {
        return equipmentRequestRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public EquipmentRequestDTO createRequest(EquipmentRequestDTO requestDTO) {
        EquipmentRequest equipmentRequest = convertToEntity(requestDTO);
        EquipmentRequest savedRequest = equipmentRequestRepository.save(equipmentRequest);
        return convertToDTO(savedRequest);
    }

    // EquipmentRequestController
    // EquipmentRequestRestController
    public EquipmentRequestDTO updateRequest(Long id, EquipmentRequestDTO requestDTO) {
        if (equipmentRequestRepository.existsById(id)) {
            EquipmentRequest equipmentRequest = convertToEntity(requestDTO);
            equipmentRequest.setEquipId(id);
            EquipmentRequest updatedRequest = equipmentRequestRepository.save(equipmentRequest);
            return convertToDTO(updatedRequest);
        } else {
            return null;
        }
    }

    public EquipmentRequestDTO updateRestRequest(Long id, EquipmentRequestDTO requestDTO) {
        if (equipmentRequestRepository.existsById(id)) {
            EquipmentRequest equipmentRequest = convertToEntity(requestDTO);
            equipmentRequest.setEquipId(id);
            EquipmentRequest updatedRequest = equipmentRequestRepository.save(equipmentRequest);
            return convertToDTO(updatedRequest);
        } else {
            return null;
        }
    }

    // EquipmentRequest 취소
    public void deleteRequest(Long id) {
        equipmentRequestRepository.deleteById(id);
    }

    public List<EquipmentRequestDTO> getPendingRequests() {
        return equipmentRequestRepository.findByApprovalStatus(ApprovalStatus.PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EquipmentRequestDTO> getCompletedRequests() {
        return equipmentRequestRepository.findByApprovalStatus(ApprovalStatus.APPROVED)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private EquipmentRequestDTO convertToDTO(EquipmentRequest equipmentRequest) {
        EquipmentRequestDTO dto = new EquipmentRequestDTO();
        dto.setEquipId(equipmentRequest.getEquipId());
        dto.setEquipName(equipmentRequest.getEquipName());
        dto.setEquipInfo(equipmentRequest.getEquipInfo());
        dto.setEquipAmount(equipmentRequest.getEquipAmount());
        dto.setEquipCost(equipmentRequest.getEquipCost());
        dto.setEquipSumcost(equipmentRequest.getEquipSumcost());
        dto.setEquipWhy(equipmentRequest.getEquipWhy());
        dto.setReqDate(equipmentRequest.getReqDate().toLocalDate()); // LocalDateTime에서 LocalDate로 변환
        dto.setKinds(equipmentRequest.getKinds());
        dto.setDel(equipmentRequest.getDel());
        dto.setApprovalStatus(equipmentRequest.getApprovalStatus());
        return dto;
    }

    private EquipmentRequest convertToEntity(EquipmentRequestDTO dto) {
        EquipmentRequest equipmentRequest = new EquipmentRequest();
        equipmentRequest.setEquipName(dto.getEquipName());
        equipmentRequest.setEquipInfo(dto.getEquipInfo());
        equipmentRequest.setEquipAmount(dto.getEquipAmount());
        equipmentRequest.setEquipCost(dto.getEquipCost());
        equipmentRequest.setEquipSumcost(dto.getEquipSumcost());
        equipmentRequest.setEquipWhy(dto.getEquipWhy());
        equipmentRequest.setReqDate(dto.getReqDate().atStartOfDay()); // LocalDate에서 LocalDateTime으로 변환
        equipmentRequest.setKinds(dto.getKinds());
        equipmentRequest.setDel(dto.getDel());
        equipmentRequest.setApprovalStatus(dto.getApprovalStatus());
        return equipmentRequest;
    }
}