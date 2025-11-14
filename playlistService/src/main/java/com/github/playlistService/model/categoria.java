package com.github.playlistService.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo que representa la categoría asociada a una playlist")
public class categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la categoría", example = "10")
    private Long idCategoria;

    @Column(nullable = false)
    @Schema(description = "Nombre de la categoría de la playlist", example = "Workout")
    private String categoriaName;
}
