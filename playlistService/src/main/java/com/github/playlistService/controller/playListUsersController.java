package com.github.playlistService.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListUsers;
import com.github.playlistService.service.playListService;
import com.github.playlistService.service.playListUsersService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api-v1/playlists-users")
@RequiredArgsConstructor
public class playListUsersController {

    private final playListUsersService playListUsersService;
    private final playListService playListService;

    //Seguir una playlist
    @PostMapping("/follow/{userId}/{playlistId}")
    public ResponseEntity<?> followPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId) {
        try {
            playList playlist = playListService.getPlaylistById(playlistId)
                    .orElseThrow(() -> new RuntimeException("Playlist con ID " + playlistId + " no encontrada."));

            playListUsers followed = playListUsersService.addPlaylistToUser(userId, playlist);
            return ResponseEntity.status(HttpStatus.CREATED).body(followed);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al seguir la playlist: " + e.getMessage());
        }
    }

    //Dejar de seguir una playlist
    @DeleteMapping("/unfollow/{userId}/{playlistId}")
    public ResponseEntity<?> unfollowPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId) {
        try {
            playListUsersService.removePlaylistFromUser(userId, playlistId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al dejar de seguir la playlist: " + e.getMessage());
        }
    }

    // Verificar si el usuario sigue una playlist
    @GetMapping("/is-following/{userId}/{playlistId}")
    public ResponseEntity<?> isFollowing(
            @PathVariable Long userId,
            @PathVariable Long playlistId) {
        try {
            boolean following = playListUsersService.isPlaylistAdded(userId, playlistId);
            return ResponseEntity.ok(following);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al verificar seguimiento: " + e.getMessage());
        }
    }

    //Obtener todas las playlists seguidas por un usuario
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

    @PostMapping("/{playlistId}/toggle-follow/{userId}")
    public ResponseEntity<String> toggleFollow(@PathVariable Long playlistId, @PathVariable Long userId) {
        String result = playListService.toggleFollow(userId, playlistId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}/followed")
    public ResponseEntity<?> getFollowed(@PathVariable Long userId) {
    return ResponseEntity.ok(playListUsersService.getUserPlaylists(userId));
}

}
