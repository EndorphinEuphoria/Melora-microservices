package com.github.playlistService.model;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "playlistSongs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Relación entre playlists y canciones")
public class playListSongs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la relación playlist-canción", example = "100")
    private Long idPlayListSong;

    @Column(nullable = false)
    @Schema(description = "ID de la canción asociada", example = "50")
    private Long songId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPlaylist", nullable = false)
    @Schema(description = "Playlist relacionada a la canción")
    private playList playlist;
}
