package com.github.playlistService.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.playlistService.DTO.SongDto;
import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListSongs;
import com.github.playlistService.service.playListSongsServices;
import com.github.playlistService.service.playListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api-v1/playlist-songs")
@RequiredArgsConstructor
public class playListSongsController {

    private final playListSongsServices playListSongsServices;
    private final playListService playListService;

    // Agregar una canción a una playlist
    @PostMapping("/{playlistId}/add/{songId}")
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {
        try {
            playList playlist = playListService.getPlaylistById(playlistId)
                    .orElseThrow(() -> new RuntimeException("Playlist con ID " + playlistId + " no encontrada"));

            playListSongs saved = playListSongsServices.addSongToPlaylist(playlist, songId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al agregar canción a playlist: " + e.getMessage());
        }
    }

    // Obtener todas las canciones de una playlist (detalles desde microservicio de música)
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<?> getSongsFromPlaylist(@PathVariable Long playlistId) {
        try {
            List<SongDto> songs = playListSongsServices.getSongsFromPlaylist(playlistId);
            if (songs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("La playlist con ID " + playlistId + " no tiene canciones.");
            }
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener canciones: " + e.getMessage());
        }
    }
}
