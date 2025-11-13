package com.github.music_service.service;

import com.github.music_service.dto.UploadRequestDto;
import com.github.music_service.model.Song;
import com.github.music_service.model.Upload;
import com.github.music_service.repository.SongRepository;
import com.github.music_service.repository.UploadRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
@Transactional
public class UploadService {

    private final SongRepository songRepository;
    private final UploadRepository uploadRepository;
    private final UserClientService userClientService;

    public Map<String, Long> uploadSong(UploadRequestDto dto) {
        // Validaciones básicas
        if (dto.getSongName() == null || dto.getSongName().isBlank())
            throw new IllegalArgumentException("Song name cannot be empty");
        if (dto.getSongPath() == null || dto.getSongPath().isBlank())
            throw new IllegalArgumentException("Song path cannot be null");
        if (dto.getSongDuration() == null || dto.getSongDuration() <= 0)
            throw new IllegalArgumentException("Invalid song duration");
        if (dto.getUserId() == null)
            throw new IllegalArgumentException("User ID cannot be null");

        // Validar existencia del usuario (REST → user-service)
        String nickname = userClientService.getNicknameByUserId(dto.getUserId());
        if (nickname.equals("Desconocido")) {
            throw new IllegalArgumentException("User not found or unavailable in user-service");
        }

        // Crear la canción
        Song song = new Song();
        song.setSongName(dto.getSongName());
        song.setSongDescription(dto.getSongDescription());
        song.setSongPath(dto.getSongPath());
        song.setCoverArt(dto.getCoverArt());
        song.setSongDuration(dto.getSongDuration());
        song.setCreationDate(dto.getCreationDate() != null
                ? dto.getCreationDate()
                : System.currentTimeMillis());

        Song savedSong = songRepository.save(song);

        // Crear la subida (upload)
        Upload upload = new Upload();
        upload.setUserId(dto.getUserId());
        upload.setSong(savedSong);
        upload.setStateId(1L);
        upload.setUploadDate(System.currentTimeMillis());

        Upload savedUpload = uploadRepository.save(upload);

        // Respuesta
        Map<String, Long> response = new HashMap<>();
        response.put("songId", savedSong.getIdSong());
        response.put("uploadId", savedUpload.getUploadId());
        return response;
    }
}

