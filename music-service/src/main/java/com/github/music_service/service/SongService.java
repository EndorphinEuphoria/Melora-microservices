package com.github.music_service.service;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;
import com.github.music_service.repository.SongRepository;
import com.github.music_service.repository.UploadRepository;

import jakarta.transaction.Transactional;
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
