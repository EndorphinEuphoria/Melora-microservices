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

    private final ObjectMapper objectMapper = new ObjectMapper();

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

        try {
            mockMvc.perform(post("/api-v1/playlists")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.playlist.idPlaylist").value(10L));
        } catch (Exception e) { e.printStackTrace(); }
    }

    // GET ALL
    @Test
    void getAllPlaylists_returnsOK() {

        when(playListService.getAllPlaylists())
                .thenReturn(List.of(new playList()));

        try {
            mockMvc.perform(get("/api-v1/playlists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e) { e.printStackTrace(); }
    }

    // GET BY ID
    @Test
    void getPlaylistById_returnsPlaylist() {

        playList playlist = new playList();
        playlist.setIdPlaylist(5L);

        when(playListService.getPlaylistById(5L))
                .thenReturn(Optional.of(playlist));

        try {
            mockMvc.perform(get("/api-v1/playlists/5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idPlaylist").value(5L));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Test
    void getPlaylistById_notFound() {

        when(playListService.getPlaylistById(99L))
                .thenReturn(Optional.empty());

        try {
            mockMvc.perform(get("/api-v1/playlists/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Playlist con ID 99 no encontrada."));
        } catch (Exception e) { e.printStackTrace(); }
    }

    // GET SONGS FROM PLAYLIST
    @Test
    void getSongsFromPlaylist_returnsOK() {

        when(playListSongsServices.getSongsFromPlaylist(1L))
                .thenReturn(List.of());

        try {
            mockMvc.perform(get("/api-v1/playlists/1/songs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e) { e.printStackTrace(); }
    }

    // PLAYLISTS BY USER
    @Test
    void getPlaylistsByUser_returnsOK() {

        when(playListService.getPlaylistsByUser(1L))
                .thenReturn(List.of(new playList()));

        try {
            mockMvc.perform(get("/api-v1/playlists/user/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Test
    void getPlaylistsByUser_notFound() {

        when(playListService.getPlaylistsByUser(2L))
                .thenReturn(List.of());

        try {
            mockMvc.perform(get("/api-v1/playlists/user/2"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("El usuario con ID 2 no tiene playlists creadas."));
        } catch (Exception e) { e.printStackTrace(); }
    }

    // SEARCH
    @Test
    void searchPlaylistByName_returnsOK() {

        when(playListService.searchPlaylistsByName("rock"))
                .thenReturn(List.of(new playList()));

        try {
            mockMvc.perform(get("/api-v1/playlists/search?name=rock"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e) { e.printStackTrace(); }
    }

    //Update
    @Test
void updatePlaylist_returnsOK() throws Exception {

    playList playlist = new playList();
    playlist.setIdPlaylist(1L);
    playlist.setFechaCreacion(LocalDateTime.now()); // si existe el campo

    when(playListService.getPlaylistById(1L))
            .thenReturn(Optional.of(playlist));

    when(playListService.updatePlaylist(any(playList.class)))
            .thenReturn(playlist);

    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    String json = objectMapper.writeValueAsString(playlist);

    mockMvc.perform(put("/api-v1/playlists/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
}


    // DELETE
    @Test
    void deletePlaylist_returnsNoContent() {

        try {
            mockMvc.perform(delete("/api-v1/playlists/1"))
                    .andExpect(status().isNoContent());
        } catch (Exception e) { e.printStackTrace(); }
    }
}
