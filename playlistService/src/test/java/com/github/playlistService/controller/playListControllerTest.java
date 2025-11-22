package com.github.playlistService.controller;

import com.github.playlistService.DTO.PlayListRequestDto;
import com.github.playlistService.model.playList;
import com.github.playlistService.service.playListService;
import com.github.playlistService.service.playListSongsServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(playListController.class)
@AutoConfigureMockMvc(addFilters = false)
public class playListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private playListService playListService;

    @MockitoBean
    private playListSongsServices playListSongsServices;

    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    // CREATE PLAYLIST
    @Test
    void createPlaylist_returnsCreated() throws Exception {

        PlayListRequestDto dto = new PlayListRequestDto();
        dto.setPlaylistName("Rock");
        dto.setUserId(1L);

        playList playlist = new playList();
        playlist.setIdPlaylist(10L);

        when(playListService.createFullPlaylist(dto)).thenReturn(playlist);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api-v1/playlists/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playlist.idPlaylist").value(10L));
    }

    // GET ALL
    @Test
    void getAllPlaylists_returnsOK() throws Exception {

        when(playListService.getAllPlaylists())
                .thenReturn(List.of(new playList()));

        mockMvc.perform(get("/api-v1/playlists/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // GET BY ID
    @Test
    void getPlaylistById_returnsPlaylist() throws Exception {

        playList playlist = new playList();
        playlist.setIdPlaylist(5L);

        when(playListService.getPlaylistById(5L))
                .thenReturn(Optional.of(playlist));

        mockMvc.perform(get("/api-v1/playlists/get/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPlaylist").value(5L));
    }

    @Test
    void getPlaylistById_notFound() throws Exception {

        when(playListService.getPlaylistById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api-v1/playlists/get/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Playlist con ID 99 no encontrada."));
    }

    // GET SONGS FROM PLAYLIST
    @Test
    void getSongsFromPlaylist_returnsOK() throws Exception {

        when(playListSongsServices.getSongsFromPlaylist(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api-v1/playlists/get/1/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // PLAYLISTS BY USER
    @Test
    void getPlaylistsByUser_returnsOK() throws Exception {

        when(playListService.getPlaylistsByUser(1L))
                .thenReturn(List.of(new playList()));

        mockMvc.perform(get("/api-v1/playlists/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPlaylistsByUser_notFound() throws Exception {

        when(playListService.getPlaylistsByUser(2L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api-v1/playlists/user/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("El usuario con ID 2 no tiene playlists creadas."));
    }

    // SEARCH
    @Test
    void searchPlaylistByName_returnsOK() throws Exception {

        when(playListService.searchPlaylistsByName("rock"))
                .thenReturn(List.of(new playList()));

        mockMvc.perform(get("/api-v1/playlists/search?name=rock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // UPDATE
    @Test
    void updatePlaylist_returnsOK() throws Exception {

        playList playlist = new playList();
        playlist.setIdPlaylist(1L);
        playlist.setFechaCreacion(LocalDateTime.now());

        when(playListService.getPlaylistById(1L))
                .thenReturn(Optional.of(playlist));

        when(playListService.updatePlaylist(any()))
                .thenReturn(playlist);

        String json = objectMapper.writeValueAsString(playlist);

        mockMvc.perform(put("/api-v1/playlists/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    // DELETE
    @Test
    void deletePlaylist_returnsNoContent() throws Exception {

        mockMvc.perform(delete("/api-v1/playlists/delete/1"))
                .andExpect(status().isNoContent());
    }
}
