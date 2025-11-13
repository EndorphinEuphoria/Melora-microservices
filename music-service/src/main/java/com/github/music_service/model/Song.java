package com.github.music_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "songs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa una canción en el sistema.")

public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la canción")
    private Long idSong;

    @Column(nullable = false)
    @Schema(description = "Nombre de la canción", example = "Shape of You")
    private String songName;

    @Column(nullable = true)
    @Schema(description = "Descripción de la canción", example = "Versión acústica")
    private String songDescription;

    @Column(nullable = false)
    @Schema(description = "Ruta del archivo de audio", example = "/music/song123.mp3")
    private String songPath;

    @Column
    @Schema(description = "Imagen de portada de la canción", example = "/images/cover123.png")
    private String coverArt;

    @Column(nullable = false)
    @Schema(description = "Duración de la canción en segundos", example = "240")
    private Integer songDuration;

    @Column(nullable = false)
    @Schema(description = "Fecha de creación en formato timestamp", example = "1715123456123")
    private Long creationDate;
}