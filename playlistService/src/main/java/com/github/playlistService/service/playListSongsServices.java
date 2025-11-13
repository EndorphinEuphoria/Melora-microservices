package com.github.playlistService.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.playlistService.DTO.SongDto;
import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListSongs;
import com.github.playlistService.repository.playListSongsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class playListSongsServices {

    @Autowired
    private playListSongsRepository playListSongsRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String SONG_SERVICE_URL = "http://localhost:8082/api-v1/songs/";

    public boolean songExists(Long songId) {
        try {
            Object song = restTemplate.getForObject(SONG_SERVICE_URL + songId, Object.class);
            return song != null;
        } catch (Exception e) {
            return false;
        }
    }

    public playListSongs addSongToPlaylist(playList playlist, Long songId) {
        if (!songExists(songId)) {
            throw new RuntimeException("La canción con ID " + songId + " no existe en el microservicio de música.");
        }

        playListSongs relation = new playListSongs();
        relation.setSongId(songId);
        relation.setPlaylist(playlist);

        return playListSongsRepository.save(relation);
    }

    // Obtener canciones por playlist 
    public List<SongDto> getSongsFromPlaylist(Long playlistId) {
        List<playListSongs> relations = playListSongsRepository.findByPlaylist_IdPlaylist(playlistId);

        return relations.stream()
                .map(r -> {
                    String url = SONG_SERVICE_URL + r.getSongId();
                    SongDto song = restTemplate.getForObject(url, SongDto.class);
                    return song;
                })
                .toList();
    }
}
