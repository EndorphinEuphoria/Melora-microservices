package com.github.music_service.controller;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;
import com.github.music_service.service.SongService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api-v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    //  CREAR CANCIÓN 
    @Operation(
            summary = "Crear una nueva canción",
            description = "Crea una canción nueva en la base de datos. Este endpoint es utilizado por el cliente-orquesta."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canción creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos enviados por el cliente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Song song) {
        try {
            Long id = songService.create(song);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("songId", id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No se pudo crear la canción: " + e.getMessage()));
        }
    }

    //  LISTAR DETALLADAS 
    @Operation(
            summary = "Obtener todas las canciones detalladas",
            description = "Devuelve información completa de todas las canciones."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<SongDetailedDto>> all() {
        return ResponseEntity.ok(songService.getAllDetailed());
    }

    // EXISTE
    @Operation(
            summary = "Comprobar si una canción existe por su ID",
            description = "Devuelve true o false dependiendo de si la canción está registrada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado entregado correctamente")
    })
    @GetMapping("/exists/{songId}")
    public ResponseEntity<Boolean> exists(@PathVariable Long songId) {
        return ResponseEntity.ok(songService.existsById(songId));
    }

    //  BUSCAR POR NOMBRE 
    @Operation(
            summary = "Buscar canciones por nombre",
            description = "Filtra canciones que contienen el texto enviado."
    )
    @GetMapping("/search")
    public ResponseEntity<List<SongDetailedDto>> search(@RequestParam(defaultValue = "") String q) {
        return ResponseEntity.ok(songService.searchByName(q));
    }

    //  OBTENER POR ID 
    @Operation(summary = "Obtener canción detallada por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canción encontrada"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @GetMapping("/{songId}")
    public ResponseEntity<?> byId(@PathVariable Long songId) {
        try {
            return ResponseEntity.ok(songService.getDetailedById(songId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Canción no encontrada: " + songId));
        }
    }

    //  LISTAR POR ARTISTA 
    @Operation(summary = "Listar canciones por ID de artista")
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<?> byArtist(@PathVariable Long artistId) {
        return ResponseEntity.ok(songService.getByArtist(artistId));
    }

    //  PATCH 
    @Operation(
            summary = "Actualizar nombre o descripción de una canción",
            description = "Permite modificar parcialmente campos específicos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canción actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Canción no encontrada")
    })
    @PatchMapping("/{songId}")
    public ResponseEntity<?> patch(
            @PathVariable Long songId,
            @RequestBody Map<String, String> body
    ) {
        try {
            songService.updatePartial(songId, body.get("songName"), body.get("songDescription"));
            return ResponseEntity.noContent().build();  // 204 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    //  ELIMINAR 
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


}