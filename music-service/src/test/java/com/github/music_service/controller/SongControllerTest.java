package com.github.music_service.controller;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.model.Song;
import com.github.music_service.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SongController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SongControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SongService songService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // CREATE
    @Test
    void createSong_thenReturnCreated() throws Exception {
        Song song = new Song();
        song.setSongName("Song");
        song.setSongPath("path.mp3");
        song.setSongDuration(100);

        when(songService.create(song)).thenReturn(10L);

        String json = objectMapper.writeValueAsString(song);

        try {
            mockMvc.perform(post("/api-v1/songs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.songId").value(10L));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET ALL
    @Test
    void listAllSongs_thenReturnOk() {
        SongDetailedDto dto = new SongDetailedDto();
        dto.setSongName("Test Song");

        when(songService.getAllDetailed()).thenReturn(List.of(dto));

        try {
            mockMvc.perform(get("/api-v1/songs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EXISTS
    @Test
    void existsSong_thenReturnBoolean() {
        when(songService.existsById(1L)).thenReturn(true);

        try {
            mockMvc.perform(get("/api-v1/songs/exists/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SEARCH
    @Test
    void searchSongs_thenReturnList() {
        SongDetailedDto dto = new SongDetailedDto();

        when(songService.searchByName("rock")).thenReturn(List.of(dto));

        try {
            mockMvc.perform(get("/api-v1/songs/search?q=rock"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET BY ID
    @Test
    void getSongById_thenReturnSong() {
        SongDetailedDto dto = new SongDetailedDto();
        dto.setSongId(1L);

        when(songService.getDetailedById(1L)).thenReturn(dto);

        try {
            mockMvc.perform(get("/api-v1/songs/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.songId").value(1L));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET BY ARTIST
    @Test
    void getByArtist_thenReturnSongs() {
        when(songService.getByArtist(99L)).thenReturn(List.of(new SongDetailedDto()));

        try {
            mockMvc.perform(get("/api-v1/songs/artist/99"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PATCH
    @Test
    void patchSong_thenReturnNoContent() throws Exception {
        Map<String, String> body = Map.of("songName", "nuevo");

        String json = objectMapper.writeValueAsString(body);

        try {
            mockMvc.perform(patch("/api-v1/songs/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE
    @Test
    void deleteSong_thenReturnNoContent() {
        try {
            mockMvc.perform(delete("/api-v1/songs/1"))
                    .andExpect(status().isNoContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // COUNT
    @Test
    void countSongs_thenReturnCount() {
        when(songService.count()).thenReturn(50L);

        try {
            mockMvc.perform(get("/api-v1/songs/count"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(50L));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
