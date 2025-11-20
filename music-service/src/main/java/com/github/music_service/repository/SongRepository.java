package com.github.music_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("""
        SELECT new com.github.music_service.dto.SongDetailedDto(
            up.userId,
            s.songName,
            s.coverArt,
            s.songDescription,
            NULL,            
            s.songDuration,
            up.uploadDate,
            NULL,
            s.idSong
        )
        FROM Song s
        JOIN Upload up ON up.song = s
        WHERE LOWER(s.songName) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
    List<SongDetailedDto> searchByName(@Param("q") String q);

    @Query("""
    SELECT new com.github.music_service.dto.SongDetailedDto(
        up.userId,
        s.songName,
        s.coverArt,
        s.songDescription,
        NULL,             
        s.songDuration,
        up.uploadDate,
        NULL,
        s.idSong
    )
    FROM Song s
    JOIN Upload up ON up.song = s
    WHERE s.idSong = :songId
""")
SongDetailedDto getLightById(@Param("songId") Long songId);



    @Query("""
        SELECT new com.github.music_service.dto.SongDetailedDto(
            up.userId,
            s.songName,
            s.coverArt,
            s.songDescription,
            NULL,             
            s.songDuration,
            up.uploadDate,
            NULL,
            s.idSong
        )
        FROM Song s
        JOIN Upload up ON up.song = s
    """)
    List<SongDetailedDto> findAllDetailed();

    @Query("""
        SELECT new com.github.music_service.dto.SongDetailedDto(
            up.userId,
            s.songName,
            s.coverArt,
            s.songDescription,
            NULL,              
            s.songDuration,
            up.uploadDate,
            NULL,
            s.idSong
        )
        FROM Song s
        JOIN Upload up ON up.song = s
        WHERE up.userId = :artistId
    """)
    List<SongDetailedDto> findByArtist(@Param("artistId") Long artistId);


    @Query("""
        SELECT new com.github.music_service.dto.SongDetailedDto(
            up.userId,
            s.songName,
            s.coverArt,
            s.songDescription,
            s.songPathBase64,  
            s.songDuration,
            up.uploadDate,
            NULL,
            s.idSong
        )
        FROM Song s
        JOIN Upload up ON up.song = s
        WHERE s.idSong = :songId
    """)
    SongDetailedDto getDetailedById(@Param("songId") Long songId);

    Optional<Song> findBySongName(String songName);
}
