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

    @Schema(description = "Portada de la canción")
    private byte [] coverArt;

    @Schema(description = "Descripción de la canción")
    private String songDescription;

    @Schema(description = "Ruta del archivo de audio")
    private byte [] songPathBase64;

    @Schema(description = "Duración de la canción en segundos")
    private Integer durationSong;

    @Schema(description = "Fecha de subida en timestamp")
    private Long uploadDate;

    @Schema(description = "Nickname del artista")
    private String nickname;

    @Schema(description = "ID de la canción")
    private Long songId;
}