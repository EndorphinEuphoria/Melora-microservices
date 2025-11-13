package com.github.music_service.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long idUser;
    private String nickname;
}