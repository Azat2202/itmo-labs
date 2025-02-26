package se.ifmo.is_lab1.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtDto {

    @NotNull
    private String username;

    @NotNull
    @Schema(description = "Пароль пользователя", example = "P@ssw0rd")
    private String password;
}


