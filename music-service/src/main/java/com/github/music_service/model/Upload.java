package com.github.music_service.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "upload")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa una subida de canción por un usuario.")
public class Upload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro de subida")
    private Long uploadId;

    @Column(nullable = false)
    @Schema(description = "ID del usuario que subió la canción")
    private Long userId;

    @Column
    @Schema(description = "Fecha de la subida en timestamp", example = "1715124000000")
    private Long uploadDate = System.currentTimeMillis();

    @Column
    @Schema(description = "Motivo del baneo (si aplica)", example = "Contenido inapropiado")
    private String banReason;

    @Column(nullable = false)
    @Schema(description = "Estado del upload (1=Activo, 2=Baneado, etc.)")
    private Long stateId = 1L;

    @Column
    @Schema(description = "Fecha del baneo en timestamp", example = "1715999999999")
    private Long banDate;

    @ManyToOne
    @JoinColumn(name = "idSong", referencedColumnName = "idSong")
    @Schema(description = "Canción asociada al upload")
    private Song song;
}


