package com.github.music_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO utilizado para solicitar la creación de una subida de canción.")
public class UploadRequestDto {

    @Schema(description = "ID del usuario que sube la canción")
    private Long userId;
    
    @Schema(description = "Nombre de la canción")
    private String songName;

    @Schema(description = "Descripción de la canción")
    private String songDescription;

    @Schema(description = "Ruta del archivo de audio")
    private String songPathBase64;

    @Schema(description = "Portada de la canción")
    private String coverArt;

    @Schema(description = "Duración de la canción en segundos")
    private Integer songDuration;

    @Schema(description = "Fecha de creación en timestamp")
    private Long creationDate;
}