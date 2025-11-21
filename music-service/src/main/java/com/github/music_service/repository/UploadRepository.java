package com.github.music_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.music_service.model.Upload;

import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {
    @Query("SELECT u FROM Upload u WHERE u.userId = :userId")
    List<Upload> findByUserId(Long userId);

    @Query("SELECT u FROM Upload u WHERE u.banReason IS NOT NULL")
    List<Upload> findBanned();

    @Query("SELECT u FROM Upload u WHERE u.song.idSong = :songId")
    List<Upload> findBySongId(Long songId);
    
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Upload u WHERE u.song.idSong = :songId")
    void deleteBySongId(Long songId);

}

