package com.github.playlistService.service;

import com.github.playlistService.DTO.UserDto;
import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListUsers;
import com.github.playlistService.repository.playListUsersRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class playListUsersServiceTest {

    @Mock
    private playListUsersRepository playListUsersRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private playListUsersService playListUsersService;

    @Test
    void getUserById_returnsUserDto() {
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenReturn(new Object());

        UserDto result = playListUsersService.getUserById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void addPlaylistToUser_returnsSavedRelation() {
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenReturn(new Object());

        playList playlist = new playList();
        playListUsers rel = new playListUsers();

        when(playListUsersRepository.save(any())).thenReturn(rel);

        playListUsers result = playListUsersService.addPlaylistToUser(1L, playlist);

        assertThat(result).isEqualTo(rel);
    }

    @Test
    void removePlaylist_invokesRepo() {
        playListUsersService.removePlaylistFromUser(1L, 2L);
        verify(playListUsersRepository)
                .deleteByUserIdAndPlaylist_IdPlaylist(1L, 2L);
    }

    @Test
    void isPlaylistAdded_returnsTrue() {
        when(playListUsersRepository.existsByUserIdAndPlaylist_IdPlaylist(1L, 5L))
                .thenReturn(true);

        assertThat(playListUsersService.isPlaylistAdded(1L, 5L))
                .isTrue();
    }
}
