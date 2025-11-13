package com.github.register_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para transferencia de datos de usuario en operaciones de autenticación y registro")
public class UserDto {

    @Schema(description = "ID del usuario (opcional en creación, obligatorio en actualización)", example = "1")
    private Long id;

    @Schema(description = "Correo electrónico del usuario", example = "lukas@example.com")
    private String email;

    @Schema(description = "Contraseña del usuario en texto plano (solo para entrada en login/registro)", example = "MiClaveSegura123")
    private String password;

    @Schema(description = "ID del rol asociado al usuario", example = "1")
    private Long rolId;
}
