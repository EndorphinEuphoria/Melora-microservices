package com.github.music_service.service;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;
import com.github.music_service.repository.SongRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserClientService userClientService;

    @InjectMocks
    private SongService songService;

    

    // GET ALL DETAILED
    @Test
    void getAllDetailed_returnsDetailedSongs() {
        SongDetailedDto dto = new SongDetailedDto();
        dto.setArtistId(10L);

        List<SongDetailedDto> list = Arrays.asList(dto);

        when(songRepository.findAllDetailed()).thenReturn(list);
        when(userClientService.getNicknameByUserId(10L)).thenReturn("Lukas");

        List<SongDetailedDto> result = songService.getAllDetailed();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNickname()).isEqualTo("Lukas");
    }

    // SEARCH BY NAME
    @Test
    void searchByName_returnsFilteredSongs() {
        SongDetailedDto dto = new SongDetailedDto();
        dto.setArtistId(5L);

        when(songRepository.searchByName("test")).thenReturn(Arrays.asList(dto));
        when(userClientService.getNicknameByUserId(5L)).thenReturn("UserNick");

        List<SongDetailedDto> result = songService.searchByName("test");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNickname()).isEqualTo("UserNick");
    }

    // GET BY ARTIST
    @Test
    void getByArtist_returnsSongsOfArtist() {
        SongDetailedDto dto = new SongDetailedDto();
        dto.setArtistId(99L);

        when(songRepository.findByArtist(99L)).thenReturn(Arrays.asList(dto));
        when(userClientService.getNicknameByUserId(99L)).thenReturn("ArtistNick");

        List<SongDetailedDto> result = songService.getByArtist(99L);

        assertThat(result.get(0).getNickname()).isEqualTo("ArtistNick");
    }

    // GET DETAILED BY ID
    @Test
    void getDetailedById_returnsSong() {
        SongDetailedDto dto = new SongDetailedDto();
        dto.setArtistId(77L);

        when(songRepository.getDetailedById(1L)).thenReturn(dto);
        when(userClientService.getNicknameByUserId(77L)).thenReturn("Artist77");

        SongDetailedDto result = songService.getDetailedById(1L);

        assertThat(result.getNickname()).isEqualTo("Artist77");
    }

    // UPDATE PARTIAL
    @Test
    void updatePartial_updatesFields() {
        Song s = new Song();
        s.setSongName("Old");
        s.setSongDescription("OldDesc");

        when(songRepository.findById(1L)).thenReturn(Optional.of(s));

        songService.updatePartial(1L, "New", "NewDesc");

        verify(songRepository).save(s);

        assertThat(s.getSongName()).isEqualTo("New");
        assertThat(s.getSongDescription()).isEqualTo("NewDesc");
    }

    // DELETE
    @Test
    void deleteById_invokesRepository() {
        songService.deleteById(1L);

        verify(songRepository).deleteById(1L);
    }

    // EXISTS
    @Test
    void existsById_returnsBoolean() {
        when(songRepository.existsById(1L)).thenReturn(true);

        boolean exists = songService.existsById(1L);

        assertThat(exists).isTrue();
    }
}
