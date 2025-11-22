package com.github.playlistService.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListUsers;
import com.github.playlistService.service.playListService;
import com.github.playlistService.service.playListUsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api-v1/playlists-users")
@RequiredArgsConstructor
public class playListUsersController {

    private final playListUsersService playListUsersService;
    private final playListService playListService;

    @Operation(summary = "Este endpoint permite a un usuario seguir una playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED: La playlist ha sido añadida a la lista de seguimiento.",
                    content = @Content(schema = @Schema(implementation = playListUsers.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error al intentar seguir la playlist.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/follow/{userId}/{playlistId}")
    public ResponseEntity<?> followPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId) {

        try {
            playList playlist = playListService.getPlaylistById(playlistId)
                    .orElseThrow(() ->
                            new RuntimeException("Playlist con ID " + playlistId + " no encontrada."));

            playListUsers followed = playListUsersService.addPlaylistToUser(userId, playlist);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("mensaje", "Playlist seguida correctamente.");
            body.put("relacion", followed);

            return ResponseEntity.status(HttpStatus.CREATED).body(body);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al seguir la playlist: " + e.getMessage());
        }
    }

    @Operation(summary = "Este endpoint permite a un usuario dejar de seguir una playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "NO CONTENT: Se dejó de seguir la playlist correctamente."),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error al dejar de seguir la playlist.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/unfollow/{userId}/{playlistId}")
    public ResponseEntity<?> unfollowPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId) {

        try {
            playListUsersService.removePlaylistFromUser(userId, playlistId);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dejar de seguir la playlist: " + e.getMessage());
        }
    }

    @Operation(summary = "Este endpoint permite verificar si un usuario sigue una playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Resultado obtenido correctamente.",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST: Error al verificar el seguimiento.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/is-following/{userId}/{playlistId}")
    public ResponseEntity<?> isFollowing(
            @PathVariable Long userId,
            @PathVariable Long playlistId) {

        try {
            boolean following = playListUsersService.isPlaylistAdded(userId, playlistId);

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("usuarioId", userId);
            body.put("playlistId", playlistId);
            body.put("siguiendo", following);

            return ResponseEntity.ok(body);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al verificar seguimiento: " + e.getMessage());
        }
    }

    @Operation(summary = "Este endpoint permite obtener todas las playlists que un usuario sigue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Playlists obtenidas correctamente.",
                    content = @Content(schema = @Schema(implementation = playListUsers.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND: El usuario no sigue ninguna playlist.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPlaylists(@PathVariable Long userId) {

        try {
            List<playListUsers> followedPlaylists = playListUsersService.getUserPlaylists(userId);

            if (followedPlaylists.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El usuario con ID " + userId + " no sigue ninguna playlist.");
            }

            return ResponseEntity.ok(followedPlaylists);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al obtener playlists seguidas: " + e.getMessage());
        }
    }

    @Operation(summary = "Este endpoint permite seguir o dejar de seguir una playlist automáticamente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Estado actualizado correctamente.",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/{playlistId}/toggle-follow/{userId}")
    public ResponseEntity<String> toggleFollow(
            @PathVariable Long playlistId,
            @PathVariable Long userId) {

        String result = playListService.toggleFollow(userId, playlistId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Este endpoint permite obtener solo las playlists seguidas por un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK: Resultado obtenido correctamente."),
    })
    @GetMapping("/user/{userId}/followed")
    public ResponseEntity<?> getFollowed(@PathVariable Long userId) {
        return ResponseEntity.ok(playListUsersService.getUserPlaylists(userId));
    }
}