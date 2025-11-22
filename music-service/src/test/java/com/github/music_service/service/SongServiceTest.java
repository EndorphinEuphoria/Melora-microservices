package com.github.music_service.service;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;
import com.github.music_service.model.Upload;
import com.github.music_service.repository.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock private SongRepository songRepository;
    @Mock private UserClientService userClientService;
    @Mock private UploadRepository uploadRepository;
    @Mock private FavoriteRepository favoriteRepository;
    @Mock private org.springframework.web.client.RestTemplate restTemplate;

    @InjectMocks
    private SongService songService;

    @Test
    void getLightById_returnsMappedSong() {
        SongDetailedDto dto = new SongDetailedDto();
        dto.setArtistId(1L);
        dto.setCoverArt(new byte[]{1,2,3});

        when(songRepository.getLightById(10L)).thenReturn(dto);
        when(userClientService.getNicknameByUserId(1L)).thenReturn("Lukas");

        SongDetailedDto result = songService.getLightById(10L);

        assertEquals("Lukas", result.getNickname());
        assertNull(result.getCoverArt());
        assertNull(result.getAudioBase64());
        assertNotNull(result.getCoverArtBase64());
    }

    @Test
    void getAllDetailed_processesList() {
        SongDetailedDto dto = new SongDetailedDto();
        dto.setArtistId(1L);
        dto.setCoverArt(new byte[]{1});
        dto.setSongPathBase64(new byte[]{2});

        when(songRepository.findAllDetailed()).thenReturn(List.of(dto));
        when(userClientService.getNicknameByUserId(1L)).thenReturn("Lukas");

        List<SongDetailedDto> result = songService.getAllDetailed();

        assertThat(result).hasSize(1);
        assertEquals("Lukas", result.get(0).getNickname());
        assertNull(result.get(0).getCoverArt());
        assertNull(result.get(0).getSongPathBase64());
        assertNotNull(result.get(0).getAudioBase64());
    }

    @Test
    void updatePartial_updatesFields() {
        Song song = new Song();
        song.setSongName("old");
        song.setSongDescription("desc");

        when(songRepository.findById(10L)).thenReturn(Optional.of(song));

        songService.updatePartial(10L, "newName", "");

        verify(songRepository).save(song);

        assertEquals("newName", song.getSongName());
        assertNull(song.getSongDescription());
    }

    @Test
    void updatePartial_throwsIfNameBlank() {
        Song song = new Song();
        when(songRepository.findById(10L)).thenReturn(Optional.of(song));

        assertThrows(IllegalArgumentException.class, () ->
            songService.updatePartial(10L, "   ", null)
        );
    }

    @Test
    void banSong_updatesUploadAndDeletes() {
        Song song = new Song();
        Upload upload = new Upload();
        upload.setSong(song);

        when(uploadRepository.findBySongId(10L)).thenReturn(List.of(upload));

        songService.banSong(10L, "bad content");

        assertEquals(2L, upload.getStateId());
        assertEquals("bad content", upload.getBanReason());

        verify(uploadRepository).save(upload);
        verify(favoriteRepository).deleteBySongId(10L);
        verify(songRepository).delete(song);
    }

    @Test
    void banSong_throwsWhenUploadNotFound() {
        when(uploadRepository.findBySongId(10L)).thenReturn(List.of());

        assertThrows(NoSuchElementException.class, () ->
            songService.banSong(10L, "reason")
        );
    }

    @Test
    void deleteById_removesSongAndUploads() {
        Song song = new Song();
        when(songRepository.findById(10L)).thenReturn(Optional.of(song));

        songService.deleteById(10L);

        verify(uploadRepository).deleteBySongId(10L);
        verify(songRepository).delete(song);
    }

    @Test
    void deleteSongsByUser_deleteMultiple() {
        Song s1 = new Song();
        s1.setIdSong(10L);

        Upload u1 = new Upload();
        u1.setSong(s1);

        when(uploadRepository.findByUserId(1L)).thenReturn(List.of(u1));

        songService.deleteSongsByUser(1L);

        verify(favoriteRepository).deleteBySongId(10L);
        verify(songRepository).delete(s1);
        verify(uploadRepository).delete(u1);
    }
}
