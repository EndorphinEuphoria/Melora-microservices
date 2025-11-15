package com.github.login_service.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class User extends RepresentationModel<User>{

    @Schema(description = "ID único del usuario")
    private Long idUser;

    @Schema(description = "Nickname único del usuario", example = "LukasDS1")
    private String nickname;
    
    @Schema(description = "Correo electrónico único del usuario", example = "lukas@example.com")
    private String email;

    @Schema(description = "Contraseña del usuario (encriptada en base de datos)", example = "********")
    private String password;

    @Schema(description = "ID del rol asignado al usuario")
    private Rol rol;
}
