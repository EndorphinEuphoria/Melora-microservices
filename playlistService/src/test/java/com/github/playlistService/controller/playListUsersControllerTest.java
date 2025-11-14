package com.github.playlistService.controller;

import com.github.playlistService.model.playList;
import com.github.playlistService.model.playListUsers;
import com.github.playlistService.service.playListService;
import com.github.playlistService.service.playListUsersService;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(playListUsersController.class)
@AutoConfigureMockMvc(addFilters = false)
public class playListUsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private playListUsersService playListUsersService;

    @MockitoBean
    private playListService playListService;


    // FOLLOW
    @Test
    void followPlaylist_returnsCreated() throws Exception {

        playList playlist = new playList();
        playListUsers relation = new playListUsers();

        when(playListService.getPlaylistById(1L))
                .thenReturn(Optional.of(playlist));

        when(playListUsersService.addPlaylistToUser(1L, playlist))
                .thenReturn(relation);

        try {
            mockMvc.perform(post("/api-v1/playlists-users/follow/1/1"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.relacion").exists());
        } catch (Exception e) { e.printStackTrace(); }
    }

    // UNFOLLOW
    @Test
    void unfollowPlaylist_returnsNoContent() {

        try {
            mockMvc.perform(delete("/api-v1/playlists-users/unfollow/1/1"))
                    .andExpect(status().isNoContent());
        } catch (Exception e){ e.printStackTrace(); }
    }

    // IS FOLLOWING
    @Test
    void isFollowing_returnsOK() {

        when(playListUsersService.isPlaylistAdded(1L, 1L))
                .thenReturn(true);

        try {
            mockMvc.perform(get("/api-v1/playlists-users/is-following/1/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.siguiendo").value(true));
        } catch (Exception e){ e.printStackTrace(); }
    }

    // GET USER PLAYLISTS
    @Test
    void getUserPlaylists_returnsList() {

        when(playListUsersService.getUserPlaylists(1L))
                .thenReturn(List.of(new playListUsers()));

        try {
            mockMvc.perform(get("/api-v1/playlists-users/user/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        } catch (Exception e){ e.printStackTrace(); }
    }

    // TOGGLE FOLLOW
    @Test
    void toggleFollow_returnsOK() {

        when(playListService.toggleFollow(1L, 10L))
                .thenReturn("Agregado");

        try {
            mockMvc.perform(post("/api-v1/playlists-users/10/toggle-follow/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Agregado"));
        } catch (Exception e){ e.printStackTrace(); }
    }
}
