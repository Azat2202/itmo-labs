package se.ifmo.is_lab1.messages.authentication;

import lombok.Data;
import se.ifmo.is_lab1.models.enums.Role;

@Data
public class UserResponse {
    private String username;
    private Role role;
}
