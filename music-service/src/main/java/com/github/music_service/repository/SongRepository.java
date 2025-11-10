package com.github.music_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.music_service.model.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long>{
    Optional<Song> getByNameOptional(String songName);
}
