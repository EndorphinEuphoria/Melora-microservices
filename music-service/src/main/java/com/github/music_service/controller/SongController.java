package com.github.music_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.music_service.model.Song;
import com.github.music_service.service.SongService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("api-v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        return ResponseEntity.status(HttpStatus.OK).body(songService.getAllSongs());
    }

    @GetMapping("/{idSong}")
    public ResponseEntity<Song> getSongById(@PathVariable Long idSong) {
        return ResponseEntity.status(HttpStatus.OK).body(songService.getSongById(idSong));
    }

    @GetMapping("/{songName}")
    public ResponseEntity<Song> getSongByName(@PathVariable String songName) {
        return ResponseEntity.status(HttpStatus.OK).body(songService.getSongByName(songName));
    }

    @PutMapping("/update/{idSong}")
    public ResponseEntity<Song> updateSong(@PathVariable Long idSong, @RequestBody Song song) {
        return ResponseEntity.status(HttpStatus.OK).body(songService.updateSong(song));
    }
    
    @DeleteMapping("/delete/{idSong}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long idSong) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
}
