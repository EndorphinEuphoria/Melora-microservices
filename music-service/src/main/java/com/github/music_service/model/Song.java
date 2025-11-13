package com.github.music_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "songs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idSong;
 
    @Column(nullable = false)
    public String songName;

    @Column(nullable = true)
    public String songDescription;
    
    @Column(nullable = false)
    public String songPath;
    
    @Column
    public String coverArt;
    
    @Column(nullable = false)
    public Integer songDuration;
    
    @Column(nullable = false)
    public Long creationDate;
}
