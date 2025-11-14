package com.github.login_service.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para la clase rol, representa el rol del usuario")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Rol extends RepresentationModel<Rol> {
    @Schema(description = "ID único del rol")
    private Long idRol;

    @Schema(description = "Nombre del rol")
    private String nombre;
}