//package ac.su.erp.controller;
//
//import ac.su.erp.dto.EquipmentRequestDTO;
//import ac.su.erp.service.EquipmentRequestService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//public class EquipmentRequestRestController {
//
//    private final EquipmentRequestService equipmentRequestService;
//
//    @GetMapping("/api/v2/equipment-requests/{id}")
//    public EquipmentRequestDTO getEquipmentRequestById(@PathVariable Long id) {
//        return equipmentRequestService.getRequestById(id);
//    }
//
//    @PutMapping("/api/v2/equipment-requests/{id}")
//    public ResponseEntity<EquipmentRequestDTO> updateRestRequest(@PathVariable Long id, @RequestBody EquipmentRequestDTO requestDTO) {
//        EquipmentRequestDTO updatedRequest = equipmentRequestService.updateRequest(id, requestDTO);
//        if (updatedRequest != null) {
//            return ResponseEntity.ok(updatedRequest);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
//
