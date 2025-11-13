package com.github.playlistService.DTO;

import lombok.Data;

@Data
public class SongDto {
    private Long songId;
    private String songName;
    private String coverArt;
    private String songDescription;
    private String songPath;
    private Integer durationSong;
    private Long uploadDate;
    private String nickname;
    private Long artistId;
}
