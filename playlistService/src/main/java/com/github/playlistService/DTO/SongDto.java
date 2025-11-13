package com.github.playlistService.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la información detallada de una canción asociada a una playlist")
public class SongDto {

    @Schema(description = "ID único de la canción", example = "50")
    private Long songId;

    @Schema(description = "Nombre de la canción", example = "Sunset Dreams")
    private String songName;

    @Schema(description = "URL o nombre del archivo de carátula", example = "cover123.png")
    private String coverArt;

    @Schema(description = "Descripción o detalles de la canción", example = "Canción instrumental de estilo chill")
    private String songDescription;

    @Schema(description = "Ruta de almacenamiento del archivo de audio", example = "/songs/user12/sunset_dreams.mp3")
    private String songPath;

    @Schema(description = "Duración de la canción en segundos", example = "215")
    private Integer durationSong;

    @Schema(description = "Fecha de subida expresada en timestamp", example = "1731523200000")
    private Long uploadDate;

    @Schema(description = "Nickname del artista dueño de la canción", example = "LukasBeats")
    private String nickname;

    @Schema(description = "ID del artista que creó o subió la canción", example = "7")
    private Long artistId;
}

