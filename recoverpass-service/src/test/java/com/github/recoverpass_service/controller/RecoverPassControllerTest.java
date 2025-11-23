package com.github.recoverpass_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.recoverpass_service.model.ResetToken;
import com.github.recoverpass_service.model.User;
import com.github.recoverpass_service.repository.ResetTokenRepository;
import com.github.recoverpass_service.service.RecoverPassService;

@WebMvcTest(RecoverPassController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RecoverPassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecoverPassService recoverPassService;

    @MockitoBean
    private ResetTokenRepository resetTokenRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    // --------------------------
    // POST /request
    // --------------------------
    @Test
    void requestReset_existingUser_returnsOk() throws Exception {
        User user = new User();
        user.setEmail("test@test.cl");

        when(recoverPassService.processRequest("test@test.cl")).thenReturn(true);

        mockMvc.perform(post("/api-v1/reset-password/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("A reset link has been sent."))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void requestReset_nonExistingUser_returnsOkWithMessage() throws Exception {
        User user = new User();
        user.setEmail("nonexist@test.cl");

        when(recoverPassService.processRequest("nonexist@test.cl")).thenReturn(false);

        mockMvc.perform(post("/api-v1/reset-password/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.status").value(true));
    }

    // --------------------------
    // GET /validateToken/{token}
    // --------------------------
    @Test
    void validateToken_validToken_returnsOk() throws Exception {
        ResetToken token = new ResetToken();
        token.setToken("valid123");
        token.setExpiresAt(java.time.LocalDateTime.now().plusMinutes(10));

        when(resetTokenRepository.findByToken("valid123")).thenReturn(Optional.of(token));

        mockMvc.perform(get("/api-v1/reset-password/validateToken/valid123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid"));
    }

    @Test
    void validateToken_invalidOrExpiredToken_returnsBadRequest() throws Exception {
        when(resetTokenRepository.findByToken("invalid")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api-v1/reset-password/validateToken/invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or expired token"));
    }

    // --------------------------
    // PUT /reset/{token}
    // --------------------------
    @Test
    void resetPassword_validToken_returnsOk() throws Exception {
        Map<String, String> body = Map.of("newPassword", "newpass123");

        doNothing().when(recoverPassService).resetPassword("token123", "newpass123");

        mockMvc.perform(put("/api-v1/reset-password/reset/token123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully"));

        verify(recoverPassService).resetPassword("token123", "newpass123");
    }
}
