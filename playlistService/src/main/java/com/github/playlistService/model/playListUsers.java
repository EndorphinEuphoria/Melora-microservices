package com.github.playlistService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "playListUsers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class playListUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idPlayListUser;

    @Column(nullable = false)
    private Long userId;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPlaylist", nullable = false)
    private playList playlist;



}
