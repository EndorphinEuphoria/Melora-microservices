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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(summary = "Agrega una canción a una playlist existente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CREATED: Canción agregada correctamente.",
                content = @Content(schema = @Schema(implementation = playListSongs.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error al agregar la canción.",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
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

   
    @Operation(summary = "Obtiene todas las canciones pertenecientes a una playlist.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Lista obtenida correctamente.")
    })
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<List<SongDto>> getSongsFromPlaylist(
            @PathVariable Long playlistId) {

        List<SongDto> songs = playListSongsServices.getSongsFromPlaylist(playlistId);

        return ResponseEntity.ok(songs);
    }

    @Operation(summary = "Elimina una canción de todas las playlists en las que esté incluida.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO CONTENT: Canción eliminada correctamente.")
    })
    @DeleteMapping("/deleteBySong/{songId}")
    public ResponseEntity<Void> deleteBySong(@PathVariable Long songId) {
        playListSongsServices.deleteBySong(songId);
        return ResponseEntity.noContent().build();
    }

}
