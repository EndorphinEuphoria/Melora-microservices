package com.github.music_service.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "upload")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Upload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uploadId;

    @Column(nullable = false)
    private Long userId; 

    @Column
    private Long uploadDate = System.currentTimeMillis();

    @Column
    private String banReason;

    @Column(nullable = false)
    private Long stateId = 1L; 

    @Column
    private Long banDate;

    @ManyToOne
    @JoinColumn(name = "idSong", referencedColumnName = "idSong")
    private Song song; 
}


