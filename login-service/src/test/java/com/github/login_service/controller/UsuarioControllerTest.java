package com.github.login_service.controller;

import com.github.login_service.model.User;
import com.github.login_service.service.UserService;

import jakarta.servlet.ServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // LOGIN: Usuario válido 202 Accepted
    @Test
    void validateUsuario_whenValidUser_thenReturnAccepted() throws Exception {

        User user = new User();
        user.setEmail("usuario@mail.com");
        user.setPassword("1234");

        when(userService.validateUser("usuario@mail.com", "1234"))
                .thenReturn(true);

        String json = objectMapper.writeValueAsString(user);

        try {
            mockMvc.perform(
                    post("/api-v1/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
            )
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.self")
                .value("http://localhost:8083/api-v1/login"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOGIN: Usuario inválido 400 Bad Request con mensaje
    @Test
void validateUsuario_whenServiceThrowsException_thenThrowRuntime() throws Exception {

    User user = new User();
    user.setEmail("crash@mail.com");
    user.setPassword("123");

    when(userService.validateUser("crash@mail.com", "123"))
            .thenThrow(new RuntimeException("Fallo interno"));

    String json = objectMapper.writeValueAsString(user);

    ServletException ex = assertThrows(ServletException.class, () ->
        mockMvc.perform(
                post("/api-v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
    );

    Throwable cause = ex.getCause();
    assertTrue(cause instanceof RuntimeException);

    assertTrue(cause.getMessage().contains("Fallo interno"));
}

}
