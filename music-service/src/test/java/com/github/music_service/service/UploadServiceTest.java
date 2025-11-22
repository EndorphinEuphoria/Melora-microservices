package com.github.music_service.service;

import com.github.music_service.dto.UploadRequestDto;
import com.github.music_service.model.Song;
import com.github.music_service.model.Upload;
import com.github.music_service.repository.SongRepository;
import com.github.music_service.repository.UploadRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class UploadServiceTest {

    @Mock private SongRepository songRepository;
    @Mock private UploadRepository uploadRepository;
    @Mock private UserClientService userClientService;

    @InjectMocks
    private UploadService uploadService;

    @Test
    void uploadSong_savesSongAndUpload() {
        UploadRequestDto dto = new UploadRequestDto();
        dto.setSongName("test");
        dto.setSongPathBase64(Base64.getEncoder().encodeToString("audio".getBytes()));
        dto.setSongDuration(120);
        dto.setUserId(1L);

        when(userClientService.getNicknameByUserId(1L)).thenReturn("Lukas");

        Song savedSong = new Song(1L,null,null,null,null,100,null);
        when(songRepository.save(any())).thenReturn(savedSong);

        Upload savedUpload = new Upload(1L,1L,null,null,1L,null,null);
        when(uploadRepository.save(any())).thenReturn(savedUpload);

        Map<String,Long> result = uploadService.uploadSong(dto);

        assertEquals(1L, result.get("songId"));
        assertEquals(1L, result.get("uploadId"));

        verify(songRepository).save(any(Song.class));
        verify(uploadRepository).save(any(Upload.class));
    }

    @Test
    void uploadSong_throwsIfInvalidSongName() {
        UploadRequestDto dto = new UploadRequestDto();
        dto.setSongName(" ");

        assertThrows(IllegalArgumentException.class, () ->
            uploadService.uploadSong(dto)
        );
    }

    @Test
    void uploadSong_throwsIfUserNotFound() {
        UploadRequestDto dto = new UploadRequestDto();
        dto.setSongName("ok");
        dto.setSongPathBase64("xx");
        dto.setSongDuration(100);
        dto.setUserId(1L);

        when(userClientService.getNicknameByUserId(1L)).thenReturn("Desconocido");

        assertThrows(IllegalArgumentException.class, () ->
            uploadService.uploadSong(dto)
        );
    }
}
