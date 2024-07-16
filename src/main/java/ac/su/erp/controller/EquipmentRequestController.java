package ac.su.erp.controller;

import ac.su.erp.dto.EquipmentRequestDTO;
import ac.su.erp.service.EquipmentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-requests")
@RequiredArgsConstructor
public class EquipmentRequestController {

    private final EquipmentRequestService equipmentRequestService;

    @GetMapping
    public ResponseEntity<List<EquipmentRequestDTO>> getAllRequests() {
        List<EquipmentRequestDTO> requests = equipmentRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentRequestDTO> getRequestById(@PathVariable Long id) {
        EquipmentRequestDTO request = equipmentRequestService.getRequestById(id);
        if (request != null) {
            return ResponseEntity.ok(request);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<EquipmentRequestDTO> createRequest(@ModelAttribute EquipmentRequestDTO requestDTO) {
        // 기본값 설정
        requestDTO.setDefaultValues();
        EquipmentRequestDTO createdRequest = equipmentRequestService.createRequest(requestDTO);
        return ResponseEntity.ok(createdRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentRequestDTO> updateRequest(@PathVariable Long id, @RequestBody EquipmentRequestDTO requestDTO) {
        EquipmentRequestDTO updatedRequest = equipmentRequestService.updateRequest(id, requestDTO);
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
