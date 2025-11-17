package com.github.register_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Este DTO esta enfocado para inicio de sesion")
@Data
public class LoginRequestDto {

    @Schema(description = "Email del usuario")
    private String email;
    @Schema(description = "Contraseña del usuario")
    private String password;
}
