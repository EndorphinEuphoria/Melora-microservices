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

    // ✔ ENDPOINT CORRECTO QUE DEVUELVE LA CANCIÓN COMPLETA
    private static final String SONG_SERVICE_URL = "http://localhost:8084/api-v1/songs/";

    // ✔ ENDPOINT QUE VERIFICA EXISTENCIA (BOOLEANO)
    private static final String SONG_EXISTS_URL = "http://localhost:8084/api-v1/songs/exists/";

    // --------------------------------------------------------------------
    // ✔ VERIFICAR SI LA CANCIÓN EXISTE (BOOLEANO)
    // --------------------------------------------------------------------
    public boolean songExists(Long songId) {
        try {
            Boolean exists = restTemplate.getForObject(SONG_EXISTS_URL + songId, Boolean.class);
            return exists != null && exists;
        } catch (Exception e) {
            return false;
        }
    }

    // --------------------------------------------------------------------
    // ✔ AGREGAR CANCIÓN A PLAYLIST
    // --------------------------------------------------------------------
    public playListSongs addSongToPlaylist(playList playlist, Long songId) {
        if (!songExists(songId)) {
            throw new RuntimeException("La canción con ID " + songId + " no existe en el microservicio de música.");
        }

        playListSongs relation = new playListSongs();
        relation.setSongId(songId);
        relation.setPlaylist(playlist);

        return playListSongsRepository.save(relation);
    }

    // --------------------------------------------------------------------
    // ⭐ OBTENER CANCIONES DE UNA PLAYLIST (CORREGIDO)
    // --------------------------------------------------------------------
    public List<SongDto> getSongsFromPlaylist(Long playlistId) {

        List<playListSongs> relations =
                playListSongsRepository.findByPlaylist_IdPlaylist(playlistId);

        return relations.stream()
                .map(r -> {
                    // ✔ LLAMADA CORRECTA: devuelve SongDetailedDto como JSON → SongDto
                    String url = SONG_SERVICE_URL + r.getSongId();
                    return restTemplate.getForObject(url, SongDto.class);
                })
                .toList();
    }
}
