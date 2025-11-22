package com.github.music_service.service;

import com.github.music_service.model.Favorite;
import com.github.music_service.repository.FavoriteRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private SongService songService;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void addFavorite_savesIfNotExists() {
        when(favoriteRepository.existsByUserIdAndSongId(1L, 10L)).thenReturn(false);

        favoriteService.addFavorite(1L, 10L);

        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addFavorite_doesNotSaveIfExists() {
        when(favoriteRepository.existsByUserIdAndSongId(1L, 10L)).thenReturn(true);

        favoriteService.addFavorite(1L, 10L);

        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void removeFavorite_deletesRecord() {
        favoriteService.removeFavorite(1L, 10L);

        verify(favoriteRepository).deleteByUserIdAndSongId(1L, 10L);
    }

    @Test
    void toggleFavorite_whenAlreadyExists_returnsFalse() {
        when(favoriteRepository.existsByUserIdAndSongId(1L, 10L)).thenReturn(true);

        boolean result = favoriteService.toggleFavorite(1L, 10L);

        verify(favoriteRepository).deleteByUserIdAndSongId(1L, 10L);
        assertFalse(result);
    }

    @Test
    void toggleFavorite_whenNotExists_returnsTrue() {
        when(favoriteRepository.existsByUserIdAndSongId(1L, 10L)).thenReturn(false);

        boolean result = favoriteService.toggleFavorite(1L, 10L);

        verify(favoriteRepository).save(any(Favorite.class));
        assertTrue(result);
    }

    @Test
    void isFavorite_returnsCorrectValue() {
        when(favoriteRepository.existsByUserIdAndSongId(1L, 10L)).thenReturn(true);

        assertTrue(favoriteService.isFavorite(1L, 10L));
    }

    @Test
    void getFavoriteSongIds_returnsList() {
        List<Favorite> favorites = Arrays.asList(
                new Favorite(1L, 1L, 10L),
                new Favorite(2L, 1L, 20L)
        );

        when(favoriteRepository.findByUserId(1L)).thenReturn(favorites);

        List<Long> result = favoriteService.getFavoriteSongIds(1L);

        assertThat(result).containsExactly(10L, 20L);
    }
}
