package ac.su.erp.controller;

import ac.su.erp.dto.EquipmentRequestDTO;
import ac.su.erp.service.EquipmentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/equipment-requests")
@RequiredArgsConstructor
public class EquipmentRequestController {

    private final EquipmentRequestService equipmentRequestService;

    @GetMapping
    public ResponseEntity<List<EquipmentRequestDTO>> getAllRequests() {
        List<EquipmentRequestDTO> requests = equipmentRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentRequestDTO> getEquipmentRequestById(@PathVariable Long id) {
        EquipmentRequestDTO equipmentRequestDTO = equipmentRequestService.getRequestById(id);
        if (equipmentRequestDTO != null) {
            return ResponseEntity.ok(equipmentRequestDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<EquipmentRequestDTO> createRequest(@RequestBody EquipmentRequestDTO requestDTO) {
        // 기본값 설정
        requestDTO.setDefaultValues();
        try {
            EquipmentRequestDTO createdRequest = equipmentRequestService.createRequest(requestDTO);
            return ResponseEntity.ok(createdRequest);
        } catch (Exception e) {
            // 서버 로그에 예외 메시지 기록
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<EquipmentRequestDTO> updateEquipmentRequest(@PathVariable Long id, @RequestBody EquipmentRequestDTO equipmentRequestDTO) {
        EquipmentRequestDTO updatedRequest = equipmentRequestService.updateRequest(id, equipmentRequestDTO);
        if (updatedRequest != null) {
            return ResponseEntity.ok(updatedRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        equipmentRequestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
