package com.github.music_service.dto;
import lombok.*;
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class SongDetailedDto {
    private Long artistId;        
    private String songName;
    private String coverArt;    
    private String songDescription;
    private String songPath;
    private Integer durationSong;
    private Long uploadDate;      
    private String nickname;     
    private Long songId;
}
