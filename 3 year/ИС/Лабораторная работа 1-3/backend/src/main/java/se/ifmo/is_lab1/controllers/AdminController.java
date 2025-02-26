package se.ifmo.is_lab1.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.ifmo.is_lab1.messages.adminPanel.AdminProposalResponse;
import se.ifmo.is_lab1.messages.authentication.UserResponse;
import se.ifmo.is_lab1.services.AdminProposalService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "Панель администратора")
public class AdminController {
    private final AdminProposalService adminProposalService;

    @GetMapping("/proposal")
    public List<AdminProposalResponse> getAdminProposals() {
        return adminProposalService.getAllAdminProposals();
    }

    @PutMapping("/proposal/{proposalId}")
    public UserResponse approveAdminProposal(@PathVariable("proposalId") Long proposalId) {
        return adminProposalService.approveAdmin(proposalId);
    }

    @DeleteMapping("/proposal/{proposalId}")
    public void declineAdminProposal(@PathVariable("proposalId") Long proposalId) {
        adminProposalService.declineAdmin(proposalId);
    }


}
