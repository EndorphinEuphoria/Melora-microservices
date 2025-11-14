package com.github.playlistService.controller;

import com.github.playlistService.DTO.SongDto;
import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListSongs;
import com.github.playlistService.service.playListSongsServices;
import com.github.playlistService.service.playListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(playListSongsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class playListSongsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private playListSongsServices playListSongsServices;

    @MockitoBean
    private playListService playListService;


    // ADD SONG TO PLAYLIST
    @Test
    void addSongToPlaylist_returnsCreated() throws Exception {

        playList playlist = new playList();
        playListSongs relation = new playListSongs();

        when(playListService.getPlaylistById(1L))
                .thenReturn(Optional.of(playlist));

        when(playListSongsServices.addSongToPlaylist(playlist, 10L))
                .thenReturn(relation);

        try {
            mockMvc.perform(post("/api-v1/playlist-songs/1/add/10"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.playlistSong").exists());
        } catch (Exception e){ e.printStackTrace(); }
    }

    // GET SONGS FROM PLAYLIST
    @Test
    void getSongsFromPlaylist_returnsList() {

        when(playListSongsServices.getSongsFromPlaylist(1L))
                .thenReturn(List.of(new SongDto()));

        try {
            mockMvc.perform(get("/api-v1/playlist-songs/1/songs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e){ e.printStackTrace(); }
    }
}
