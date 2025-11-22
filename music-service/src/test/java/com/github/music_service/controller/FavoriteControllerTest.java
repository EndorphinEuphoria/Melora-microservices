package com.github.music_service.controller;

import com.github.music_service.dto.SongDetailedDto;
import com.github.music_service.service.FavoriteService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoriteService favoriteService;


    @Test
    void toggleFavorite_returnsBoolean() throws Exception {
        when(favoriteService.toggleFavorite(1L, 10L)).thenReturn(true);

        mockMvc.perform(post("/api-v1/favorites/toggle/{userId}/{songId}", 1L, 10L))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

 
    @Test
    void isFavorite_returnsBoolean() throws Exception {
        when(favoriteService.isFavorite(1L, 10L)).thenReturn(false);

        mockMvc.perform(get("/api-v1/favorites/is-favorite/{userId}/{songId}", 1L, 10L))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

 
    @Test
    void getIds_returnsListOfIds() throws Exception {
        List<Long> ids = Arrays.asList(10L, 20L, 30L);
        when(favoriteService.getFavoriteSongIds(1L)).thenReturn(ids);

        mockMvc.perform(get("/api-v1/favorites/user/{userId}/ids", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(10L))
                .andExpect(jsonPath("$[1]").value(20L))
                .andExpect(jsonPath("$[2]").value(30L));
    }

    @Test
    void getFavorites_returnsSongDtoList() throws Exception {
        SongDetailedDto dto1 = new SongDetailedDto();
        dto1.setSongId(10L);

        SongDetailedDto dto2 = new SongDetailedDto();
        dto2.setSongId(20L);

        when(favoriteService.getFavoriteSongs(1L)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api-v1/favorites/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].songId").value(10L))
                .andExpect(jsonPath("$[1].songId").value(20L));
    }


    @Test
    void removeFavorite_returnsNoContent() throws Exception {
        doNothing().when(favoriteService).removeFavorite(1L, 10L);

        mockMvc.perform(delete("/api-v1/favorites/{userId}/{songId}", 1L, 10L))
                .andExpect(status().isNoContent());
    }
}
