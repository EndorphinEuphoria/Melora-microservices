package com.github.playlistService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.playlistService.model.playListUsers;

@Repository
public interface playListUsersRepository extends JpaRepository<playListUsers,Long> {
   // Buscar todas las playlists de un usuario
    List<playListUsers> findByUserId(Long userId);

    // Eliminar una relación usuario-playlist
    void deleteByUserIdAndPlaylist_IdPlaylist(Long userId, Long idPlaylist);

    // Verificar si ya existe la relación usuario-playlist
    boolean existsByUserIdAndPlaylist_IdPlaylist(Long userId, Long idPlaylist);
}
