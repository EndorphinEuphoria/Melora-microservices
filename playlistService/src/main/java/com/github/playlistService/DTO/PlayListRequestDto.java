package com.github.playlistService.DTO;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para crear una nueva playlist con categoría, acceso y canciones asociadas")
public class PlayListRequestDto {

    @Schema(description = "Nombre de la playlist a crear", example = "Mis Favoritas 2025")
    private String playlistName;

    @Schema(description = "ID del usuario creador de la playlist", example = "12")
    private Long userId;

    @Schema(description = "ID de la categoría asignada a la playlist", example = "3")
    private Long categoriaId;

    @Schema(description = "ID del tipo de acceso asignado (público, privado, etc.)", example = "1")
    private Long accesoId;

    @Schema(description = "Lista de IDs de canciones que formarán parte de la playlist", example = "[10, 12, 45]")
    private List<Long> songIds;
}
