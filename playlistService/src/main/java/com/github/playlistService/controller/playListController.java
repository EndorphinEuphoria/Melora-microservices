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


    @Operation(summary = "Este endpoint permite crear una playlist completa con categoría, acceso y canciones.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "CREATED: Playlist creada correctamente.",
                content = @Content(schema = @Schema(implementation = playList.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error al crear la playlist.",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
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

    @Operation(summary = "Obtiene todas las playlists registradas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Listado devuelto correctamente.",
                content = @Content(schema = @Schema(implementation = playList.class)))
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<playList>> getAllPlaylists() {
        return ResponseEntity.ok(playListService.getAllPlaylists());
    }


    @Operation(summary = "Obtiene una playlist por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Playlist encontrada.",
                content = @Content(schema = @Schema(implementation = playList.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND: No existe la playlist.",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getPlaylistById(@PathVariable Long id) {

        return playListService.getPlaylistById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Playlist con ID " + id + " no encontrada."));
    }

    @Operation(summary = "Obtiene todas las canciones de una playlist.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Canciones obtenidas correctamente.")
    })
    @GetMapping("/get/{id}/songs")
    public ResponseEntity<?> getSongsFromPlaylist(@PathVariable Long id) {
        return ResponseEntity.ok(playListSongsServices.getSongsFromPlaylist(id));
    }


    @Operation(summary = "Obtiene todas las playlists creadas por un usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Listado obtenido.",
                content = @Content(schema = @Schema(implementation = playList.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND: El usuario no tiene playlists.",
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


    @Operation(summary = "Busca playlists por coincidencia de nombre.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Resultados devueltos correctamente.")
    })
    @GetMapping("/search")
    public ResponseEntity<List<playList>> searchPlaylistsByName(@RequestParam String name) {
        return ResponseEntity.ok(playListService.searchPlaylistsByName(name));
    }


    @Operation(summary = "Actualiza una playlist completa por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Playlist actualizada correctamente.",
                content = @Content(schema = @Schema(implementation = playList.class))),
        @ApiResponse(responseCode = "404", description = "NOT FOUND: No existe la playlist.",
                content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error al actualizar.",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
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


    @Operation(summary = "Elimina una playlist por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO CONTENT: Eliminada correctamente."),
        @ApiResponse(responseCode = "404", description = "NOT FOUND: No existe la playlist.",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
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

    @Operation(summary = "Elimina todas las playlists y relaciones asociadas a un usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO CONTENT: Datos limpiados correctamente.")
    })
    @DeleteMapping("/cleanup/user/{userId}")
    public ResponseEntity<?> cleanupAllUserData(@PathVariable Long userId) {
        playListService.cleanupUserData(userId);
        return ResponseEntity.noContent().build();
    }
}
