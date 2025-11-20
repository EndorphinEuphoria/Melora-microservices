package com.github.music_service.service;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Favorite;
import com.github.music_service.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional  
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final SongService songService;

    // Agregar favorito
    public void addFavorite(Long userId, Long songId) {
        if (!favoriteRepository.existsByUserIdAndSongId(userId, songId)) {
            favoriteRepository.save(new Favorite(null, userId, songId));
        }
    }

    // Eliminar favorito
    public void removeFavorite(Long userId, Long songId) {
        favoriteRepository.deleteByUserIdAndSongId(userId, songId);
    }

    // Toggle favorito
    public boolean toggleFavorite(Long userId, Long songId) {
        boolean exists = favoriteRepository.existsByUserIdAndSongId(userId, songId);

        if (exists) {
            removeFavorite(userId, songId);
            return false;
        } else {
            addFavorite(userId, songId);
            return true;
        }
    }

    // Verificar si es favorito
    public boolean isFavorite(Long userId, Long songId) {
        return favoriteRepository.existsByUserIdAndSongId(userId, songId);
    }

    // Obtener solo IDs
    public List<Long> getFavoriteSongIds(Long userId) {
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(Favorite::getSongId)
                .toList();
    }
    

    public List<SongDetailedDto> getFavoriteSongs(Long userId) {
    return getFavoriteSongIds(userId)
        .stream()
        .map(songService::getLightById) // este NO trae audio
        .toList();
}


}
