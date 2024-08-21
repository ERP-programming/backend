package ac.su.erp.controller;

import ac.su.erp.constant.ApprovalStatus;
import ac.su.erp.dto.EquipmentRequestDTO;
import ac.su.erp.service.EquipmentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WorkflowController {

    private final EquipmentRequestService equipmentRequestService;

    @GetMapping("/workflow")
    public String showWorkflow(Model model) {
        List<EquipmentRequestDTO> pendingItems = equipmentRequestService.getPendingRequests().stream().map(request -> {
            request.setMeta(request.getApprover() != null ? request.getApprover() : "미승인");
            request.setApprovalStatus(request.getApprovalStatus());
            return request;
        }).collect(Collectors.toList());

        List<EquipmentRequestDTO> completedItems = equipmentRequestService.getCompletedRequests().stream().map(request -> {
            request.setMeta(request.getApprover() != null ? request.getApprover() : " ");
            request.setApprovalStatus(request.getApprovalStatus());
            return request;
        }).collect(Collectors.toList());

        long pendingCount = pendingItems.stream().filter(item -> item.getApprovalStatus() == ApprovalStatus.PENDING).count();

        model.addAttribute("pendingItems", pendingItems);
        model.addAttribute("completedItems", completedItems);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("equipmentRequest", new EquipmentRequestDTO());

        return "Workflow"; // Thymeleaf 템플릿 경로
    }

    @GetMapping("/workflow/edit/{equipId}")
    public String showEditForm(@PathVariable Long equipId, Model model) {
        EquipmentRequestDTO equipmentRequest = equipmentRequestService.getEquipmentRequestById(equipId);
        model.addAttribute("equipmentRequest", equipmentRequest);
        return "editEquipmentRequest"; // 비품 수정 폼의 Thymeleaf 템플릿 경로
    }

    @PostMapping("/workflow/edit/{equipId}")
    public String handleEditForm(@PathVariable Long equipId, @ModelAttribute EquipmentRequestDTO equipmentRequestDTO) {
        // 비품 정보 업데이트 로직
        equipmentRequestDTO.setId(equipId); // 수정할 비품의 ID 설정
        equipmentRequestService.updateEquipmentRequest(equipmentRequestDTO); // 서비스를 통해 비품 정보 업데이트
        return "redirect:/workflow"; // 수정 후 다시 Workflow 페이지로 리다이렉트
    }
}

