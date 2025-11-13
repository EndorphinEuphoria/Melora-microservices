package com.github.playlistService.DTO;

import java.util.List;
import lombok.Data;

@Data
public class PlayListRequestDto {
    private String playlistName;
    private Long userId;
    private Long categoriaId;
    private Long accesoId;
    private List<Long> songIds;
}
