package com.github.playlistService.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo que representa una playlist creada por un usuario")
public class playList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la playlist", example = "5")
    private long idPlaylist;

    @Column(nullable = false)
    @Schema(description = "Nombre de la playlist", example = "Mis favoritas 2025")
    private String playlistName;

    @Column(nullable = false)
    @Schema(description = "ID del usuario creador de la playlist", example = "12")
    private Long userId;

    @Column(nullable = false)
    @Schema(description = "Fecha de creación de la playlist", example = "2025-11-13T19:30:00")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategoria")
    @Schema(description = "Categoría asignada a la playlist")
    private categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAcceso")
    @Schema(description = "Nivel de acceso asignado a la playlist (público, privado, etc.)")
    private acceso acceso;
}
