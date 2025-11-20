package com.github.playlistService.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.*;
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

    // -------------------------------------------------------------------
    // AGREGAR CANCIÓN A PLAYLIST
    // -------------------------------------------------------------------
    @PostMapping("/{playlistId}/add/{songId}")
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId) {

        try {
            playList playlist = playListService.getPlaylistById(playlistId)
                    .orElseThrow(() -> new RuntimeException("Playlist con ID " + playlistId + " no encontrada."));

            playListSongs saved = playListSongsServices.addSongToPlaylist(playlist, songId);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("mensaje", "Canción agregada correctamente a la playlist.");
            body.put("playlistSong", saved);

            return ResponseEntity.status(HttpStatus.CREATED).body(body);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // -------------------------------------------------------------------
    // OBTENER CANCIONES DE UNA PLAYLIST  → SIEMPRE LISTA (NO 404)
    // -------------------------------------------------------------------
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<List<SongDto>> getSongsFromPlaylist(
            @PathVariable Long playlistId) {

        List<SongDto> songs = playListSongsServices.getSongsFromPlaylist(playlistId);

        return ResponseEntity.ok(songs);
    }
}


