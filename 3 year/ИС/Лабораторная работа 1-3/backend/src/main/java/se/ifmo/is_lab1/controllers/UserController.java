package se.ifmo.is_lab1.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.is_lab1.messages.adminPanel.AdminProposalResponse;
import se.ifmo.is_lab1.messages.authentication.UserResponse;
import se.ifmo.is_lab1.services.AdminProposalService;
import se.ifmo.is_lab1.services.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Панель обычного пользователя")
public class UserController {
    private final AdminProposalService adminProposalService;
    private final UserService userService;

    @PostMapping("/proposal")
    public AdminProposalResponse becomeAdmin() {
        return adminProposalService.createAdminProposal();
    }

    @GetMapping("/me")
    public UserResponse me() {
        return userService.me();
    }
}
