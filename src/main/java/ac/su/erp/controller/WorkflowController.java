package ac.su.erp.controller;

import ac.su.erp.constant.ApprovalStatus;
import ac.su.erp.dto.EquipmentRequestDTO;
import ac.su.erp.service.EquipmentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

        return "Workflow";
    }
}

//    @GetMapping("/equipmentRequestForm")
//    public String showEquipmentRequestForm(Model model) {
//        EquipmentRequestDTO equipmentRequest = new EquipmentRequestDTO();
//        equipmentRequest.setDefaultValues(); // 기본값 설정
//        model.addAttribute("equipmentRequest", equipmentRequest);
//        return "equipmentRequestForm";
//    }
//}

