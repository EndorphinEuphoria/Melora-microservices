package com.github.playlistService.service;

import com.github.playlistService.DTO.PlayListRequestDto;
import com.github.playlistService.DTO.UserDto;
import com.github.playlistService.model.acceso;
import com.github.playlistService.model.categoria;
import com.github.playlistService.model.playList;
import com.github.playlistService.repository.accesoRepository;
import com.github.playlistService.repository.categoriaRepository;
import com.github.playlistService.repository.playListRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class playListServiceTest {

    @Mock
    private accesoRepository accesoRepository;

    @Mock
    private categoriaRepository categoriaRepository;

    @Mock
    private playListRepository playListRepository;

    @Mock
    private playListSongsServices playListSongsServices;

    @Mock
    private playListUsersService playListUsersService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private playListService playListService;

    @Test
    void userExists_returnsTrue() {
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenReturn(new Object());

        assertThat(playListService.userExists(1L))
                .isTrue();
    }

    @Test
    void getUserById_returnsDto() {
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenReturn(new Object());

        UserDto dto = playListService.getUserById(5L);

        assertThat(dto.getId()).isEqualTo(5L);
    }

    @Test
    void createFullPlaylist_savesPlaylist() {
        PlayListRequestDto dto = new PlayListRequestDto();
        dto.setPlaylistName("Rock");
        dto.setUserId(1L);
        dto.setAccesoId(1L);
        dto.setCategoriaId(1L);

        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenReturn(new Object());

        when(accesoRepository.findById(1L))
                .thenReturn(Optional.of(new acceso()));

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(new categoria()));

        playList saved = new playList();
        saved.setIdPlaylist(10L);

        when(playListRepository.save(any(playList.class)))
                .thenReturn(saved);

        playList result = playListService.createFullPlaylist(dto);

        assertThat(result.getIdPlaylist()).isEqualTo(10L);
    }

    @Test
    void toggleFollow_addsPlaylist() {
        playList playlist = new playList();
        playlist.setIdPlaylist(1L);
        playlist.setPlaylistName("Mi Lista");

        when(playListRepository.findById(1L))
                .thenReturn(Optional.of(playlist));

        when(playListUsersService.isPlaylistAdded(1L, 1L))
                .thenReturn(false);

        String msg = playListService.toggleFollow(1L, 1L);

        assertThat(msg).contains("agregada");
    }

    @Test
    void toggleFollow_removesPlaylist() {
        playList playlist = new playList();
        playlist.setIdPlaylist(1L);
        playlist.setPlaylistName("Mi Lista");

        when(playListRepository.findById(1L))
                .thenReturn(Optional.of(playlist));

        when(playListUsersService.isPlaylistAdded(1L, 1L))
                .thenReturn(true);

        String msg = playListService.toggleFollow(1L, 1L);

        assertThat(msg).contains("removida");
    }

    @Test
    void getPlaylistsByUser_returnsList() {
        when(playListRepository.findByUserId(1L))
                .thenReturn(List.of(new playList()));

        assertThat(playListService.getPlaylistsByUser(1L))
                .hasSize(1);
    }

    @Test
    void deletePlaylist_invokesRepo() {
        playListService.deletePlaylist(1L);
        verify(playListRepository).deleteById(1L);
    }
}
