package com.github.music_service.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO con información básica de un usuario.")
public class UserResponseDto {

    @Schema(description = "ID del usuario")
    private Long idUser;

    @Schema(description = "Nickname del usuario")
    private String nickname;
}