package com.github.playlistService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acceso")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que representa el nivel de acceso de una playlist")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class acceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del nivel de acceso", example = "1")
    private Long idAcceso;

    @Column(nullable = false)
    @Schema(description = "Nombre del nivel de acceso (público, privado, oculto, etc.)", example = "publico")
    private String nombre;
}
