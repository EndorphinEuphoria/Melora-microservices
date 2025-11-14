package com.github.playlistService.service;

import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListSongs;
import com.github.playlistService.repository.playListSongsRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class playListSongsServicesTest {

    @Mock
    private playListSongsRepository playListSongsRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private playListSongsServices playListSongsServices;

    @Test
    void songExists_returnsTrue() {
        when(restTemplate.getForObject(anyString(), eq(Boolean.class)))
                .thenReturn(true);

        assertThat(playListSongsServices.songExists(5L)).isTrue();
    }

    @Test
    void addSongToPlaylist_savesRelation() {
        playList playlist = new playList();
        playListSongs relation = new playListSongs();

        when(restTemplate.getForObject(anyString(), eq(Boolean.class)))
                .thenReturn(true);

        when(playListSongsRepository.save(any(playListSongs.class)))
                .thenReturn(relation);

        playListSongs result = playListSongsServices.addSongToPlaylist(playlist, 10L);

        assertThat(result).isNotNull();
    }
}
