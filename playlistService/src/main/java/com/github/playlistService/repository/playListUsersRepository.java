package com.github.playlistService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.playlistService.model.playListUsers;

@Repository
public interface playListUsersRepository extends JpaRepository<playListUsers,Long> {
    
    List<playListUsers> findByUserId(Long userId);

    void deleteByUserIdAndPlaylist_IdPlaylist(Long userId, Long idPlaylist);

    boolean existsByUserIdAndPlaylist_IdPlaylist(Long userId, Long idPlaylist);

    void deleteByPlaylist_IdPlaylist(Long playlistId);

    void deleteByUserId(Long userId);

    
}
