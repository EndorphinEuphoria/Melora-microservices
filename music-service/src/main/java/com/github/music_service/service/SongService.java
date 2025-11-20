package com.github.music_service.service;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;
import com.github.music_service.model.Upload;
import com.github.music_service.repository.SongRepository;
import com.github.music_service.repository.UploadRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SongService {
  private final SongRepository songRepository;
    private final UserClientService userClientService; 
    private final UploadRepository uploadRepository;

    private void applyBase64Conversion(SongDetailedDto s) {
        if (s.getCoverArt() != null) {
            s.setCoverArtBase64(
                Base64.getEncoder().encodeToString(s.getCoverArt())
            );
        }

        if (s.getSongPathBase64() != null) {
            s.setAudioBase64(
                Base64.getEncoder().encodeToString(s.getSongPathBase64())
            );
        }
    }

    public List<SongDetailedDto> getAllDetailed() {
        List<SongDetailedDto> songs = songRepository.findAllDetailed();
        songs.forEach(s -> {
            s.setNickname(userClientService.getNicknameByUserId(s.getArtistId()));
            applyBase64Conversion(s);
        });
        return songs;
    }

    public List<SongDetailedDto> searchByName(String q) {
        List<SongDetailedDto> songs = songRepository.searchByName(q == null ? "" : q);
        songs.forEach(s -> {
            s.setNickname(userClientService.getNicknameByUserId(s.getArtistId()));
            applyBase64Conversion(s);
        });
        return songs;
    }

    public List<SongDetailedDto> getByArtist(Long artistId) {
        List<SongDetailedDto> songs = songRepository.findByArtist(artistId);
        songs.forEach(s -> {
            s.setNickname(userClientService.getNicknameByUserId(s.getArtistId()));
            applyBase64Conversion(s);
        });
        return songs;
    }

    public SongDetailedDto getDetailedById(Long songId) {
        SongDetailedDto dto = songRepository.getDetailedById(songId);
        dto.setNickname(userClientService.getNicknameByUserId(dto.getArtistId()));
        applyBase64Conversion(dto);
        return dto;
    }


    public void updatePartial(Long songId, String newName, String newDescription) {

    Song s = songRepository.findById(songId)
            .orElseThrow(() -> new NoSuchElementException("Song not found"));

    if (newName != null) {
        if (newName.isBlank()) {
            throw new IllegalArgumentException("Song name cannot be empty");
        }
        s.setSongName(newName);
    }

    if (newDescription != null) {
        s.setSongDescription(newDescription.isBlank() ? null : newDescription);
    }

    songRepository.save(s);
}

public void banSong(Long songId, String reason) {

    if (reason == null || reason.isBlank()) {
        throw new IllegalArgumentException("Ban reason cannot be empty");
    }

    // Buscar el upload asociado a la canción
    List<Upload> uploads = uploadRepository.findBySongId(songId);

    if (uploads.isEmpty()) {
        throw new NoSuchElementException("Upload not found for song: " + songId);
    }

    Upload upload = uploads.get(0);

    // Actualizar el estado del upload
    upload.setStateId(2L);
    upload.setBanReason(reason);
    upload.setBanDate(System.currentTimeMillis());

    // *** ROMPER LA RELACIÓN PARA EVITAR TRANSIENT ERROR ***
    Song song = upload.getSong();
    upload.setSong(null);

    // Guardar upload SIN relación a Song
    uploadRepository.save(upload);

    // Finalmente borrar song
    songRepository.delete(song);
}

    public boolean existsById(Long songId) {
    return songRepository.existsById(songId);
    }

    public void deleteById(Long id) {

    Song song = songRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Song not found"));

    uploadRepository.deleteBySongId(id);

    songRepository.delete(song);
}

   
    

}
