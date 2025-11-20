package com.github.music_service.repository;

import com.github.music_service.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserIdAndSongId(Long userId, Long songId);

    void deleteByUserIdAndSongId(Long userId, Long songId);

    List<Favorite> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.songId = :songId")
    void deleteBySongId(Long songId);

    @Query("SELECT f.userId FROM Favorite f WHERE f.songId = :songId")
    List<Long> findUsersBySongId(Long songId);
}
