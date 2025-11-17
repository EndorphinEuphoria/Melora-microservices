package com.github.music_service.service;

import com.github.music_service.dto.UploadRequestDto;
import com.github.music_service.model.Song;
import com.github.music_service.model.Upload;
import com.github.music_service.repository.SongRepository;
import com.github.music_service.repository.UploadRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UploadServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private UploadRepository uploadRepository;

    @Mock
    private UserClientService userClientService;

    @InjectMocks
    private UploadService uploadService;

    @Test
    void uploadSong_savesSongAndUpload() {

        UploadRequestDto dto = new UploadRequestDto();
        dto.setSongName("Song");
        dto.setSongPathBase64("/path");
        dto.setSongDuration(200);
        dto.setUserId(10L);

        when(userClientService.getNicknameByUserId(10L)).thenReturn("UserNick");

        Song savedSong = new Song();
        savedSong.setIdSong(5L);

        when(songRepository.save(any(Song.class))).thenReturn(savedSong);

        Upload savedUpload = new Upload();
        savedUpload.setUploadId(50L);

        when(uploadRepository.save(any(Upload.class))).thenReturn(savedUpload);

        Map<String, Long> result = uploadService.uploadSong(dto);

        assertThat(result.get("songId")).isEqualTo(5L);
        assertThat(result.get("uploadId")).isEqualTo(50L);
    }
}
