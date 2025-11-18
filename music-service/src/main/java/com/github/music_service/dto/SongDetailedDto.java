package com.github.music_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO detallado de canción con datos del artista y carga.")
public class SongDetailedDto {

    @Schema(description = "ID del artista que subió la canción")
    private Long artistId;

    @Schema(description = "Nombre de la canción")
    private String songName;

    @Schema(description = "Portada de la canción (binaria)")
    private byte[] coverArt;

    @Schema(description = "Descripción de la canción")
    private String songDescription;

    @Schema(description = "Archivo de audio (binario)")
    private byte[] songPathBase64;

    @Schema(description = "Duración de la canción en segundos")
    private Integer durationSong;

    @Schema(description = "Fecha de subida en timestamp")
    private Long uploadDate;

    @Schema(description = "Nickname del artista")
    private String nickname;

    @Schema(description = "ID de la canción")
    private Long songId;
    
    private String coverArtBase64;
    private String audioBase64;

    // CONSTRUCTOR EXCLUSIVO PARA JPQL (9 parámetros)
    public SongDetailedDto(
            Long artistId,
            String songName,
            byte[] coverArt,
            String songDescription,
            byte[] songPathBase64,
            Integer durationSong,
            Long uploadDate,
            String nickname,
            Long songId
    ) {
        this.artistId = artistId;
        this.songName = songName;
        this.coverArt = coverArt;
        this.songDescription = songDescription;
        this.songPathBase64 = songPathBase64;
        this.durationSong = durationSong;
        this.uploadDate = uploadDate;
        this.nickname = nickname;
        this.songId = songId;

        // Los dos campos extra NO vienen del query → quedan null
        this.coverArtBase64 = null;
        this.audioBase64 = null;
    }
}
