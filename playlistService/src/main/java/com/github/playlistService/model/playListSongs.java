package com.github.playlistService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "playlistSongs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class playListSongs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlayListSong;

    @Column(nullable = false)
    private Long songId;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPlaylist", nullable = false)
    private playList playlist;
}
