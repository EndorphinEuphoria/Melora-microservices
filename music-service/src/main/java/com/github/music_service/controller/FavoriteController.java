package com.github.music_service.controller;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api-v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/toggle/{userId}/{songId}")
    public ResponseEntity<Boolean> toggle(
            @PathVariable Long userId,
            @PathVariable Long songId
    ) {
        return ResponseEntity.ok(favoriteService.toggleFavorite(userId, songId));
    }

    @GetMapping("/is-favorite/{userId}/{songId}")
    public ResponseEntity<Boolean> isFavorite(
            @PathVariable Long userId,
            @PathVariable Long songId
    ) {
        return ResponseEntity.ok(favoriteService.isFavorite(userId, songId));
    }

    @GetMapping("/user/{userId}/ids")
    public ResponseEntity<List<Long>> getIds(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoriteSongIds(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SongDetailedDto>> getFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getFavoriteSongs(userId));
    }

    @DeleteMapping("/{userId}/{songId}")
    public ResponseEntity<?> remove(
            @PathVariable Long userId,
            @PathVariable Long songId
    ) {
        favoriteService.removeFavorite(userId, songId);
        return ResponseEntity.noContent().build();
    }
}
