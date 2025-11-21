package com.github.playlistService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.playlistService.model.playListSongs;

@Repository
public interface playListSongsRepository extends JpaRepository<playListSongs, Long> {
     List<playListSongs> findByPlaylist_IdPlaylist(Long idPlaylist);
    void deleteByPlaylist_IdPlaylist(Long idPlaylist);

    @Modifying
    @Query("DELETE FROM playListSongs ps WHERE ps.songId = :songId")
    void deleteBySongId(@Param("songId") Long songId);

}
