package com.github.music_service.controller;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;
import com.github.music_service.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api-v1/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    // Crear canción (cliente-orquesta envía name, path, coverArt?, description?, duration, creationDate?)
    @PostMapping
    public ResponseEntity<Map<String, Long>> create(@RequestBody Song song) {
        Long id = songService.create(song);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("songId", id));
    }

    // Listar todas detalladas
    @GetMapping
    public List<SongDetailedDto> all() {
        return songService.getAllDetailed();
    }

    @GetMapping("/exists/{songId}")
    public boolean exists(@PathVariable Long songId) {
    return songService.existsById(songId);
}



    // Buscar por nombre
    @GetMapping("/search")
    public List<SongDetailedDto> search(@RequestParam(defaultValue = "") String q) {
        return songService.searchByName(q);
    }

    // Obtener por id (detallado)
    @GetMapping("/{songId}")
    public SongDetailedDto byId(@PathVariable Long songId) {
        return songService.getDetailedById(songId);
    }

    // Listar por artista
    @GetMapping("/artist/{artistId}")
    public List<SongDetailedDto> byArtist(@PathVariable Long artistId) {
        return songService.getByArtist(artistId);
    }

    // Actualización parcial (name/description)
    @PatchMapping("/{songId}")
    public ResponseEntity<Void> patch(
            @PathVariable Long songId,
            @RequestBody Map<String, String> body
    ) {
        songService.updatePartial(songId, body.get("songName"), body.get("songDescription"));
        return ResponseEntity.noContent().build();
    }

    // Eliminar por id
    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> delete(@PathVariable Long songId) {
        songService.deleteById(songId);
        return ResponseEntity.noContent().build();
    }

    // Contador simple
    @GetMapping("/count")
    public Map<String, Long> count() {
        return Map.of("count", songService.count());
    }
}
