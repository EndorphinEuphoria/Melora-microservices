package com.github.playlistService.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.playlistService.DTO.PlayListRequestDto;
import com.github.playlistService.DTO.UserDto;
import com.github.playlistService.model.playList;
import com.github.playlistService.repository.accesoRepository;
import com.github.playlistService.repository.categoriaRepository;
import com.github.playlistService.repository.playListRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class playListService {


    @Autowired
    private accesoRepository accesoRepository;

    @Autowired
    private categoriaRepository categoriaRepository;
    
    @Autowired
    private playListRepository playListRepository;
    
    @Autowired
    private playListSongsServices playListSongsServices;

    @Autowired
    private playListUsersService playListUsersService; 

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8082/api-v1/auth/exists/";

    // Verifica si el usuario existe en el microservicio de usuarios
    public boolean userExists(Long userId) {
        try {
            Object user = restTemplate.getForObject(USER_SERVICE_URL + userId, Object.class);
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }

    // Obtiene el usuario
    public UserDto getUserById(Long userId) {
        try {
            String url = USER_SERVICE_URL + userId;
            Object userResponse = restTemplate.getForObject(url, Object.class);

            if (userResponse == null) {
                throw new RuntimeException("No se encontró el usuario con ID: " + userId);
            }

            UserDto user = new UserDto();
            user.setId(userId);
            return user;

        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el usuario: " + e.getMessage());
        }
    }

    //crear playlist

 public playList createFullPlaylist(PlayListRequestDto dto) {

    if (!userExists(dto.getUserId())) {
        throw new RuntimeException("El usuario con ID " + dto.getUserId() + " no existe.");
    }

    Long accesoId = (dto.getAccesoId() != null) ? dto.getAccesoId() : 1L;
    Long categoriaId = (dto.getCategoriaId() != null) ? dto.getCategoriaId() : 1L;

    var acceso = accesoRepository.findById(accesoId)
            .orElseThrow(() -> new RuntimeException("Acceso no encontrado con ID=" + accesoId));

    var categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID=" + categoriaId));

    if (dto.getSongIds() != null) {
        for (Long songId : dto.getSongIds()) {
            if (!playListSongsServices.songExists(songId)) {
                throw new RuntimeException("La canción con ID " + songId + " no existe en el microservicio de música.");
            }
        }
    }

    playList playlist = new playList();
    playlist.setPlaylistName(dto.getPlaylistName());
    playlist.setUserId(dto.getUserId());
    playlist.setAcceso(acceso);
    playlist.setCategoria(categoria);

    playList saved = playListRepository.save(playlist);

    if (dto.getSongIds() != null) {
        for (Long songId : dto.getSongIds()) {
            playListSongsServices.addSongToPlaylist(saved, songId);
        }
    }

    playListUsersService.addPlaylistToUser(dto.getUserId(), saved);

    return saved;
}

    //  Seguir o dejar de seguir una playlist
    public String toggleFollow(Long userId, Long playlistId) {
        Optional<playList> playlistOpt = playListRepository.findById(playlistId);
        if (playlistOpt.isEmpty()) {
            throw new RuntimeException("La playlist con ID " + playlistId + " no existe.");
        }

        playList playlist = playlistOpt.get();

        boolean isFollowing = playListUsersService.isPlaylistAdded(userId, playlistId);

        if (isFollowing) {
            playListUsersService.removePlaylistFromUser(userId, playlistId);
            return "Playlist " + playlist.getPlaylistName() + " removida de tus seguidas.";
        } else {
            playListUsersService.addPlaylistToUser(userId, playlist);
            return "Playlist " + playlist.getPlaylistName() + " agregada a tus seguidas.";
        }
    }

    //Obtener playlists por usuario
    public List<playList> getPlaylistsByUser(Long userId) {
        return playListRepository.findByUserId(userId);
    }

    //Actualizar playlist
    public playList updatePlaylist(playList playlist) {
        return playListRepository.save(playlist);
    }

    // Eliminar playlist
    public void deletePlaylist(Long idPlaylist) {
        playListRepository.deleteById(idPlaylist);
    }

    //Obtener todas las playlists
    public List<playList> getAllPlaylists() {
        return playListRepository.findAll();
    }

    //Buscar por nombre
    public List<playList> searchPlaylistsByName(String query) {
        return playListRepository.findByPlaylistNameContainingIgnoreCase(query);
    }

    //Obtener por ID
    public Optional<playList> getPlaylistById(Long id) {
        return playListRepository.findById(id);
    }

        
}
