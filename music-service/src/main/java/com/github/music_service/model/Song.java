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
 // TODO añadir lenght a todo
    @Column(nullable = false)
    public String SongName;

    @Column(nullable = false)
    public String songDescription;
    
    @Column(nullable = false)
    public String songPath;
    
    @Column
    public String coverArt;
    
    @Column(nullable = false)
    public int songDuration;
    
    @Column(nullable = false)
    public Long creationDate;
}
