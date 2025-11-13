package com.github.register_service.model;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Entidad que representa el rol de un usuario dentro del sistema")
public class Rol extends RepresentationModel<Rol> { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del rol", example = "1")
    private Long idRol;

    @Column(nullable = false, length = 20)
    @Schema(description = "Nombre del rol", example = "ADMIN")
    private String nombre;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL)
    @JsonIgnore
    @Schema(description = "Lista de usuarios asociados a este rol (oculta en la serialización)")
    List<User> users;
}
