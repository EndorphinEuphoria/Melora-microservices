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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api-v1/playlists")
@RequiredArgsConstructor
public class playListController {

    private final playListService playListService;
    private final playListSongsServices playListSongsServices;


    //  CREAR PLAYLIST
    @Operation(summary = "Este endpoint permite crear playlists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED: La playlist ha sido creada correctamente.",
                    content = @Content(schema = @Schema(implementation = playList.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error en los datos enviados por el cliente.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
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

    //  OBTENER TODAS LAS PLAYLISTS
    @Operation(summary = "Este endpoint permite obtener todas las playlists registradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Listado obtenido correctamente.",
                    content = @Content(schema = @Schema(implementation = playList.class)))
    })
    @GetMapping
    public ResponseEntity<List<playList>> getAllPlaylists() {
        return ResponseEntity.ok(playListService.getAllPlaylists());
    }

    //  OBTENER PLAYLIST POR ID
    @Operation(summary = "Este endpoint permite obtener una playlist por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Playlist encontrada.",
                    content = @Content(schema = @Schema(implementation = playList.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Playlist no encontrada.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable Long id) {

        return playListService.getPlaylistById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Playlist con ID " + id + " no encontrada."));
    }

    //  OBTENER CANCIONES DE UNA PLAYLIST
    @Operation(summary = "Este endpoint permite obtener todas las canciones de una playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Canciones obtenidas correctamente.")
    })
    @GetMapping("/{id}/songs")
    public ResponseEntity<?> getSongsFromPlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(playListSongsServices.getSongsFromPlaylist(id));
    }

    //  PLAYLISTS POR USUARIO
    // 
    @Operation(summary = "Este endpoint permite obtener todas las playlists creadas por un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Playlists obtenidas correctamente.",
                    content = @Content(schema = @Schema(implementation = playList.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: El usuario no tiene playlists creadas.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPlaylistsByUser(@PathVariable Long userId) {

        List<playList> playlists = playListService.getPlaylistsByUser(userId);

        if (playlists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El usuario con ID " + userId + " no tiene playlists creadas.");
        }

        return ResponseEntity.ok(playlists);
    }

    //  BUSCAR PLAYLIST POR NOMBRE
    @Operation(summary = "Este endpoint permite buscar playlists por nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Búsqueda realizada correctamente.")
    })
    @GetMapping("/search")
    public ResponseEntity<List<playList>> searchPlaylistsByName(@RequestParam String name) {
        return ResponseEntity.ok(playListService.searchPlaylistsByName(name));
    }


    //  ACTUALIZAR PLAYLIST
    @Operation(summary = "Este endpoint permite actualizar una playlist por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Playlist actualizada correctamente.",
                    content = @Content(schema = @Schema(implementation = playList.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Playlist no encontrada.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{id}")
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

    //  ELIMINAR PLAYLIST
    @Operation(summary = "Este endpoint permite eliminar una playlist por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT: Playlist eliminada correctamente."),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: Playlist no encontrada.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{id}")
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
