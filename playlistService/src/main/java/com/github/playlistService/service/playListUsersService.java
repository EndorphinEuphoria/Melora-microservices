package com.github.playlistService.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.playlistService.DTO.UserDto;
import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListUsers;
import com.github.playlistService.repository.playListUsersRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class playListUsersService {

    @Autowired
    private playListUsersRepository playListUsersRepository;

    @Autowired
    private RestTemplate restTemplate;

   private final String USER_SERVICE_URL ="http://localhost:8082/api-v1/register/exists/";

   public UserDto getUserById(Long userId) {
    try {
        String url = USER_SERVICE_URL + userId;
        Object response = restTemplate.getForObject(url, Object.class);

        if (response == null) {
            throw new RuntimeException("No se encontró el usuario con ID: " + userId);
        }

        // Creamos y devolvemos un DTO con solo el id
        UserDto user = new UserDto();
        user.setId(userId);
        return user;

    } catch (Exception e) {
        throw new RuntimeException("Error al obtener el usuario: " + e.getMessage());
    }
}

    
    public playListUsers addPlaylistToUser(Long userId, playList playlist) {
    UserDto user = getUserById(userId); 
    if (user == null) {
        throw new RuntimeException("El usuario con ID " + userId + " no existe.");
    }

    playListUsers PlayListUsers = new playListUsers();
    PlayListUsers.setUserId(user.getId()); 
    PlayListUsers.setPlaylist(playlist);
    return playListUsersRepository.save(PlayListUsers);
}



    //Dejar de seguir playlist
    public void removePlaylistFromUser(Long userId, Long playlistId) {
        playListUsersRepository.deleteByUserIdAndPlaylist_IdPlaylist(userId, playlistId);
    }

    // Verificar si el usuario sigue una playlist
    public boolean isPlaylistAdded(Long userId, Long playlistId) {
        return playListUsersRepository.existsByUserIdAndPlaylist_IdPlaylist(userId, playlistId);
    }

    //  Obtener playlists seguidas por el usuario
    public List<playListUsers> getUserPlaylists(Long userId) {
        return playListUsersRepository.findByUserId(userId);
    }

}
