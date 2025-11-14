package com.github.music_service.controller;

import com.github.music_service.dto.UploadRequestDto;
import com.github.music_service.service.UploadService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UploadController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UploadService uploadService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // UPLOAD FULL
    @Test
    void uploadSong_thenReturnCreated() throws Exception {
        UploadRequestDto dto = new UploadRequestDto();
        dto.setSongName("Song");
        dto.setSongPath("path.mp3");
        dto.setSongDuration(120);
        dto.setUserId(1L);

        Map<String, Long> response = Map.of(
                "songId", 10L,
                "uploadId", 20L
        );

        when(uploadService.uploadSong(dto)).thenReturn(response);

        String json = objectMapper.writeValueAsString(dto);

        try {
            mockMvc.perform(post("/api-v1/uploads/full")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.songId").value(10L))
                    .andExpect(jsonPath("$.uploadId").value(20L));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPLOAD FAIL
    @Test
    void uploadSong_whenError_thenReturnBadRequest() throws Exception {
        UploadRequestDto dto = new UploadRequestDto();
        dto.setSongName("");
        dto.setSongPath("");
        dto.setSongDuration(-1);
        dto.setUserId(1L);

        when(uploadService.uploadSong(dto))
                .thenThrow(new RuntimeException("Invalid data"));

        String json = objectMapper.writeValueAsString(dto);

        try {
            mockMvc.perform(post("/api-v1/uploads/full")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
