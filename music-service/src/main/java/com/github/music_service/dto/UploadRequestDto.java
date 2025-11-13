package com.github.music_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestDto {
    private Long userId;             
    private String songName;        
    private String songDescription;  
    private String songPath;        
    private String coverArt;        
    private Integer songDuration;    
    private Long creationDate;       
}
