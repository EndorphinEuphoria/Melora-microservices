package com.github.playlistService.controller;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.http.*;
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

    // ---------------------------------------
    // 1. CREAR PLAYLIST  (POST /api-v1/playlists/create)
    // ---------------------------------------
    @PostMapping("/create")
    public ResponseEntity<?> createPlaylist(@RequestBody PlayListRequestDto dto) {
        try {
            playList created = playListService.createFullPlaylist(dto);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("mensaje", "Playlist creada correctamente.");
            body.put("playlist", created);

            return ResponseEntity.status(HttpStatus.CREATED).body(body);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear la playlist: " + e.getMessage());
        }
    }

    // ---------------------------------------
    // 2. OBTENER TODAS LAS PLAYLISTS (GET /api-v1/playlists/getAll)
    // ---------------------------------------
    @GetMapping("/getAll")
    public ResponseEntity<List<playList>> getAllPlaylists() {
        return ResponseEntity.ok(playListService.getAllPlaylists());
    }

    // ---------------------------------------
    // 3. OBTENER PLAYLIST POR ID (GET /api-v1/playlists/get/{id})
    // ---------------------------------------
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable Long id) {

        return playListService.getPlaylistById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Playlist con ID " + id + " no encontrada."));
    }

    // ---------------------------------------
    // 4. OBTENER CANCIONES DE UNA PLAYLIST (GET /api-v1/playlists/get/{id}/songs)
    // ---------------------------------------
    @GetMapping("/get/{id}/songs")
    public ResponseEntity<?> getSongsFromPlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(playListSongsServices.getSongsFromPlaylist(id));
    }

    // ---------------------------------------
    // 5. PLAYLISTS DE UN USUARIO (GET /api-v1/playlists/user/{userId})
    // ---------------------------------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPlaylistsByUser(@PathVariable Long userId) {

        List<playList> playlists = playListService.getPlaylistsByUser(userId);

        if (playlists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con ID " + userId + " no tiene playlists creadas.");
        }

        return ResponseEntity.ok(playlists);
    }

    // ---------------------------------------
    // 6. BUSCAR PLAYLIST POR NOMBRE (GET /api-v1/playlists/search?name=texto)
    // ---------------------------------------
    @GetMapping("/search")
    public ResponseEntity<List<playList>> searchPlaylistsByName(@RequestParam String name) {
        return ResponseEntity.ok(playListService.searchPlaylistsByName(name));
    }

    // ---------------------------------------
    // 7. ACTUALIZAR PLAYLIST (PUT /api-v1/playlists/update/{id})
    // ---------------------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePlaylist(@PathVariable Long id, @RequestBody playList updatedPlaylist) {

        try {
            if (playListService.getPlaylistById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la playlist con ID " + id);
            }

            updatedPlaylist.setIdPlaylist(id);
            playList updated = playListService.updatePlaylist(updatedPlaylist);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("mensaje", "Playlist actualizada correctamente.");
            body.put("playlist", updated);

            return ResponseEntity.ok(body);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar la playlist: " + e.getMessage());
        }
    }

    // ---------------------------------------
    // 8. ELIMINAR PLAYLIST (DELETE /api-v1/playlists/delete/{id})
    // ---------------------------------------
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {

        try {
            playListService.deletePlaylist(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se pudo eliminar la playlist: " + e.getMessage());
        }
    }
}
