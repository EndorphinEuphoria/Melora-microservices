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

    // AGREGAR CANCIÓN A PLAYLIST
    @Operation(summary = "Este endpoint permite agregar una canción a una playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED: La canción fue agregada correctamente a la playlist.",
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
                    .body("Error al agregar canción: " + e.getMessage());
        }
    }

    // OBTENER TODAS LAS CANCIONES DE UNA PLAYLIST
    @Operation(summary = "Este endpoint permite obtener todas las canciones asociadas a una playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Canciones obtenidas correctamente.",
                    content = @Content(schema = @Schema(implementation = SongDto.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: La playlist no tiene canciones.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<?> getSongsFromPlaylist(@PathVariable Long playlistId) {

        try {
            List<SongDto> songs = playListSongsServices.getSongsFromPlaylist(playlistId);

            if (songs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("La playlist con ID " + playlistId + " no tiene canciones asociadas.");
            }

            return ResponseEntity.ok(songs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener canciones: " + e.getMessage());
        }
    }
}
