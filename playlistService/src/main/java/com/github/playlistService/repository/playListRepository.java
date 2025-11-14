package com.github.playlistService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.playlistService.model.playList;

@Repository
public interface playListRepository extends JpaRepository<playList, Long> {
     List<playList> findByPlaylistNameContainingIgnoreCase(String playlistName);

     List<playList> findByUserId(Long userId);




}
