package com.github.playlistService.model;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "playListUsers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Relación entre usuarios y playlists (acceso compartido)")
public class playListUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la relación playlist-usuario", example = "300")
    public Long idPlayListUser;

    @Column(nullable = false)
    @Schema(description = "ID del usuario asociado a la playlist", example = "15")
    private Long userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPlaylist", nullable = false)
    @Schema(description = "Playlist a la cual pertenece este usuario")
    private playList playlist;
}
