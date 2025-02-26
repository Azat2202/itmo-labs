package se.ifmo.is_lab1.messages.adminPanel;

import lombok.Data;
import se.ifmo.is_lab1.messages.authentication.UserResponse;

@Data
public class AdminProposalResponse {
    private Long id;
    private UserResponse user;
}
