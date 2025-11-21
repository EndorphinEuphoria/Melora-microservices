package com.github.music_service.controller;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.dto.banRequestDto;
import com.github.music_service.service.SongService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api-v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    // -------------------------
    // LISTAR TODAS (LIVIANO)
    // -------------------------
    @Operation(
            summary = "Obtener todas las canciones detalladas (sin audio ni portadas pesadas)",
            description = "Ideal para listados rápidos en la app."
    )
    @GetMapping("/getAll")
    public ResponseEntity<List<SongDetailedDto>> all() {
        return ResponseEntity.ok(songService.getAllDetailed());
    }

    // -------------------------
    // VALIDAR EXISTENCIA
    // -------------------------
    @Operation(summary = "Comprobar si una canción existe por su ID")
    @GetMapping("/exists/{songId}")
    public ResponseEntity<Boolean> exists(@PathVariable Long songId) {
        return ResponseEntity.ok(songService.existsById(songId));
    }

    // -------------------------
    // BÚSQUEDA POR NOMBRE
    // -------------------------
    @Operation(summary = "Buscar canciones por nombre")
    @GetMapping("/search")
    public ResponseEntity<List<SongDetailedDto>> search(@RequestParam(defaultValue = "") String q) {
        return ResponseEntity.ok(songService.searchByName(q));
    }

    // -------------------------
    // DETALLE COMPLETO (CON AUDIO)
    // -------------------------
    @Operation(summary = "Obtener canción detallada por ID (incluye audio y portada)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canción encontrada"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @GetMapping("/{songId}")
    public ResponseEntity<?> byId(@PathVariable Long songId) {
        try {
            return ResponseEntity.ok(songService.getDetailedById(songId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Canción no encontrada con ID " + songId));
        }
    }

    @Operation(summary = "Listar canciones por ID de artista")
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<SongDetailedDto>> byArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(songService.getByArtist(artistId));
    }

  
    @Operation(summary = "Actualizar nombre o descripción de una canción")
    @PatchMapping("/{songId}")
    public ResponseEntity<?> patch(
            @PathVariable Long songId,
            @RequestBody Map<String, String> body
    ) {
        try {
            songService.updatePartial(songId, body.get("songName"), body.get("songDescription"));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // -------------------------
    // ELIMINAR CANCIÓN
    // -------------------------
    @Operation(summary = "Eliminar una canción por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canción eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @DeleteMapping("/{songId}")
    public ResponseEntity<?> delete(@PathVariable Long songId) {
        try {
            songService.deleteById(songId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // -------------------------
    // BANEAR CANCIÓN
    // -------------------------
    @PostMapping("/{songId}/ban")
    public ResponseEntity<?> banSong(
            @PathVariable Long songId,
            @RequestBody banRequestDto dto
    ) {
        try {
            songService.banSong(songId, dto.getReason());
            return ResponseEntity.ok(Map.of("message", "Song banned successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

     @Operation(summary = "Eliminar todas las canciones de un usuario (artistId)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canciones eliminadas (o no había ninguna)"),
    })
    @DeleteMapping("/by-user/{userId}")
    public ResponseEntity<?> deleteByUser(@PathVariable Long userId) {
        songService.deleteSongsByUser(userId);
        return ResponseEntity.noContent().build();
    }
}
