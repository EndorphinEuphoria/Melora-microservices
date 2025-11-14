package com.github.playlistService.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO básico de un usuario para representación en playlists")
public class UserDto {

    @Schema(description = "ID único del usuario", example = "12")
    private Long id;

    @Schema(description = "Nombre visible o nickname del usuario", example = "LukasDonoso")
    private String nickname;

    @Schema(description = "Correo electrónico del usuario", example = "lukas@example.com")
    private String email;
}
