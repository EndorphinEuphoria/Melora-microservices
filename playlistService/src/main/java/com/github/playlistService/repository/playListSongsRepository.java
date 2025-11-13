package com.github.playlistService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.playlistService.model.playListSongs;

@Repository
public interface playListSongsRepository extends JpaRepository<playListSongs, Long> {
     List<playListSongs> findByPlaylist_IdPlaylist(Long idPlaylist);
    void deleteByPlaylist_IdPlaylist(Long idPlaylist);
}
