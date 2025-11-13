package com.github.music_service.service;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;
import com.github.music_service.repository.SongRepository;
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

    public Long create(Song song) {
        if (song.getSongName() == null || song.getSongName().isBlank())
            throw new IllegalArgumentException("The Song name cannot be empty");
        if (song.getSongPath() == null || song.getSongPath().isBlank())
            throw new IllegalArgumentException("Song path is required");
        if (song.getSongDuration() == null || song.getSongDuration() <= 0)
            throw new IllegalArgumentException("Song duration must be > 0");

        if (song.getCreationDate() == null) {
            song.setCreationDate(System.currentTimeMillis());
        }

        Song saved = songRepository.save(song);
        return saved.getIdSong();
    }


    public List<SongDetailedDto> getAllDetailed() {
        List<SongDetailedDto> songs = songRepository.findAllDetailed();
        songs.forEach(s -> s.setNickname(userClientService.getNicknameByUserId(s.getArtistId())));
        return songs;
    }

    public List<SongDetailedDto> searchByName(String q) {
        List<SongDetailedDto> songs = songRepository.searchByName(q == null ? "" : q);
        songs.forEach(s -> s.setNickname(userClientService.getNicknameByUserId(s.getArtistId())));
        return songs;
    }

    public List<SongDetailedDto> getByArtist(Long artistId) {
        List<SongDetailedDto> songs = songRepository.findByArtist(artistId);
        songs.forEach(s -> s.setNickname(userClientService.getNicknameByUserId(s.getArtistId())));
        return songs;
    }

    public SongDetailedDto getDetailedById(Long songId) {
        SongDetailedDto dto = songRepository.getDetailedById(songId);
        dto.setNickname(userClientService.getNicknameByUserId(dto.getArtistId()));
        return dto;
    }

    public long count() { return songRepository.count(); }

    public void updatePartial(Long songId, String newName, String newDescription) {
        Song s = songRepository.findById(songId)
                .orElseThrow(() -> new NoSuchElementException("Song not found"));
        if (newName != null) s.setSongName(newName);
        if (newDescription != null) s.setSongDescription(newDescription);
        songRepository.save(s);
    }

    public void deleteById(Long id) {
        songRepository.deleteById(id);
    }

    public boolean existsById(Long songId) {
    return songRepository.existsById(songId);
}

}
