package com.github.playlistService.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.playlistService.DTO.PlayListRequestDto;
import com.github.playlistService.model.playList;
import com.github.playlistService.service.playListService;
import com.github.playlistService.service.playListSongsServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api-v1/playlists")
@RequiredArgsConstructor
public class playListController {

    private final playListService playListService;
    private final playListSongsServices playListSongsServices;

    @PostMapping
    public ResponseEntity<?> createPlaylist(@RequestBody PlayListRequestDto dto) {
        try {
            playList created = playListService.createFullPlaylist(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear la playlist: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<playList>> getAllPlaylists() {
        return ResponseEntity.ok(playListService.getAllPlaylists());
    }

    // Obtener playlist por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable Long id) {
        return playListService.getPlaylistById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Playlist con ID " + id + " no encontrada."));
    }

    // Obtener canciones de playlist (desde music-service)
    @GetMapping("/{id}/songs")
    public ResponseEntity<?> getSongsFromPlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(playListSongsServices.getSongsFromPlaylist(id));
    }

    // Obtener playlists creadas por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPlaylistsByUser(@PathVariable Long userId) {
        List<playList> playlists = playListService.getPlaylistsByUser(userId);

        if (playlists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con ID " + userId + " no tiene playlists creadas.");
        }

        return ResponseEntity.ok(playlists);
    }

    // Buscar por nombre
    @GetMapping("/search")
    public ResponseEntity<List<playList>> searchPlaylistsByName(@RequestParam String name) {
        return ResponseEntity.ok(playListService.searchPlaylistsByName(name));
    }

    // Actualizar playlist
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlaylist(@PathVariable Long id, @RequestBody playList updatedPlaylist) {
        try {
            if (playListService.getPlaylistById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la playlist con ID " + id);
            }

            updatedPlaylist.setIdPlaylist(id);
            return ResponseEntity.ok(playListService.updatePlaylist(updatedPlaylist));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar la playlist: " + e.getMessage());
        }
    }

    //  Eliminar playlist
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        try {
            playListService.deletePlaylist(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error al eliminar la playlist con ID " + id + ": " + e.getMessage());
        }
    }
}

