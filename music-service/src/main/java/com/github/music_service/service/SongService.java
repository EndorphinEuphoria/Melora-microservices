package com.github.music_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.music_service.model.Song;
import com.github.music_service.repository.SongRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;


    // TODO: control null exceptions with optional.empty()
    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    // Get song by id
    public Song getSongById(Long idSong) {
        return songRepository.findById(idSong).orElseThrow(() -> new RuntimeException("Song not found."));
    }

    // Get song by name TODO: format songName (trim().isnotblank etc.)
    public Song getSongByName(String songName) {
        return songRepository.getByNameOptional(songName).orElseThrow(() -> new RuntimeException("Song not found."));
    }

    // update Song info
    public Song updateSong(Song song) {
        Song newSongInfo = songRepository.findById(song.getIdSong()).orElseThrow(() -> new RuntimeException("Song not found."));
        if (song.getSongName() != null && !song.getSongName().trim().isBlank()) {
            newSongInfo.setSongName(song.getSongName());
        }

        if (song.getSongDescription() != null && !song.getSongDescription().trim().isBlank()) {
            newSongInfo.setSongDescription(song.getSongDescription());
        }

        if (song.getSongPath() != null && !song.getSongPath().trim().isBlank()) {
            newSongInfo.setSongPath(song.getSongPath());
        }

        if (song.getCoverArt() != null && !song.getCoverArt().trim().isBlank()) {
            newSongInfo.setCoverArt(song.getCoverArt());
        }

        // TODO: delete, may not be necessary
        // if (song.getSongDuration() != null) {
        //     newSongInfo.setSongDuration(0);
        // }
        return songRepository.save(newSongInfo);
    }

    public void deleteSong(Long idSong) {
        if (songRepository.existsById(idSong)) {
            songRepository.deleteById(idSong);
        }
    }
}