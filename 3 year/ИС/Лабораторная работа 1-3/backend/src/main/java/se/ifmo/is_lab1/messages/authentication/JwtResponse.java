package se.ifmo.is_lab1.messages.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

    private String token;

    private long expiresIn;

}


