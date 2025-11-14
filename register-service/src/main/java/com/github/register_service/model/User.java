package com.github.register_service.model;


import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Entidad que representa a un usuario registrado en el sistema")
public class User extends RepresentationModel<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long idUser;

    @Column(nullable = false, length = 50)
    @Schema(description = "Nombre del usuario", example = "Lukas")
    private String name;

    @Column(name = "l_name", nullable = false, length = 50)
    @JsonProperty("lName")
    @Schema(description = "Apellido del usuario", example = "Donoso")
    private String lName;

    @Column(nullable = false, length = 50)
    @Schema(description = "Nickname único del usuario", example = "LukasDS1")
    private String nickname;
    
    @Column(nullable = false, length = 100, unique = true)
    @Schema(description = "Correo electrónico único del usuario", example = "lukas@example.com")
    private String email;

    @Column(nullable = false, length = 255)
    @Schema(description = "Contraseña del usuario (encriptada en base de datos)", example = "********")
    private String password;

    @Column(nullable = true, length = 255)
    @Schema(description = "URL de la foto de perfil del usuario", example = "https://cdn.melora.com/profiles/lukas.png")
    private String profilePhotoUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    @JsonIgnoreProperties("users")
    @Schema(description = "Rol asignado al usuario")
    private Rol rol;
}
