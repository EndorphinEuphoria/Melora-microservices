package com.github.music_service.controller;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.service.FavoriteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api-v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "Activa o desactiva el estado de favorito para una canción.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Estado actualizado correctamente.",
                content = @Content(schema = @Schema(implementation = Boolean.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error al actualizar el estado.",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/toggle/{userId}/{songId}")
    public ResponseEntity<Boolean> toggle(
            @PathVariable Long userId,
            @PathVariable Long songId
    ) {
        return ResponseEntity.ok(favoriteService.toggleFavorite(userId, songId));
    }


    @Operation(summary = "Verifica si una canción es favorita para un usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Resultado obtenido correctamente.",
                content = @Content(schema = @Schema(implementation = Boolean.class)))
    })
    @GetMapping("/is-favorite/{userId}/{songId}")
    public ResponseEntity<Boolean> isFavorite(
            @PathVariable Long userId,
            @PathVariable Long songId
    ) {
        return ResponseEntity.ok(favoriteService.isFavorite(userId, songId));
    }

  
    @Operation(summary = "Obtiene solo los IDs de las canciones favoritas del usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: IDs obtenidos correctamente.",
                content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("/user/{userId}/ids")
    public ResponseEntity<List<Long>> getIds(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoriteSongIds(userId));
    }


    @Operation(summary = "Obtiene todas las canciones favoritas del usuario con información detallada.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK: Canciones obtenidas correctamente.",
                content = @Content(schema = @Schema(implementation = SongDetailedDto.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SongDetailedDto>> getFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoriteSongs(userId));
    }

    @Operation(summary = "Elimina una canción de favoritos para un usuario.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "NO CONTENT: Eliminada correctamente."),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error al eliminar.",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{userId}/{songId}")
    public ResponseEntity<?> remove(
            @PathVariable Long userId,
            @PathVariable Long songId
    ) {
        favoriteService.removeFavorite(userId, songId);
        return ResponseEntity.noContent().build();
    }
}
