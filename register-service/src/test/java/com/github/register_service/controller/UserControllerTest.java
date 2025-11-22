package com.github.register_service.controller;

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
import com.github.register_service.dto.UserDto;
import com.github.register_service.model.User;
import com.github.register_service.model.Rol;
import com.github.register_service.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper mapper = new ObjectMapper();

    // --------------------------
    // GET ALL USERS
    // --------------------------
    @Test
    void getAllUsers_returnsOK() throws Exception {
        User u1 = new User(1L, "nick1", "mail1@test.cl", "pass", null, new Rol());
        User u2 = new User(2L, "nick2", "mail2@test.cl", "pass", null, new Rol());

        when(userService.findAllUsers()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/api-v1/auth/getall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.*").exists());
    }

    // --------------------------
    // GET BY ID
    // --------------------------
    @Test
    void existsById_returnsUser() throws Exception {
        User u = new User(1L, "nick", "mail@test.cl", "pass", null, new Rol());

        when(userService.findUserById(1L)).thenReturn(u);

        mockMvc.perform(get("/api-v1/auth/exists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUser").value(1L));
    }

    // --------------------------
    // GET BY MAIL
    // --------------------------
    @Test
    void getUserByMail_returnsUser() throws Exception {
        User u = new User(1L, "nick", "test@test.cl", "pass", null, new Rol());

        when(userService.getByMail("test@test.cl")).thenReturn(Optional.of(u));

        mockMvc.perform(post("/api-v1/auth/getbymail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.cl\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.cl"));
    }

    // --------------------------
    // CREATE USER
    // --------------------------
    @Test
    void createUser_returnsCreated() throws Exception {
        UserDto dto = new UserDto();
        dto.setEmail("test@test.cl");
        dto.setPassword("pass");
        dto.setNickname("nick");
        dto.setRolId(1L);

        User saved = new User(1L, "nick", "test@test.cl", "pass", null, new Rol());

        when(userService.getByMail("test@test.cl")).thenReturn(Optional.empty());
        when(userService.saveUser(any(User.class), any())).thenReturn(saved);

        mockMvc.perform(post("/api-v1/auth/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario.email").value("test@test.cl"));
    }

    // --------------------------
    // DELETE USER
    // --------------------------
    @Test
    void deleteUser_returnsOK() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/api-v1/auth/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("El usuario ha sido borrado con éxito."));
    }

    @Test
    void updateUser_returnsOK() throws Exception {
        UserDto dto = new UserDto();
        dto.setEmail("new@mail.cl");
        dto.setPassword("newpass");
        dto.setNickname("newnick");
        dto.setRolId(2L);

        User updated = new User(10L, "newnick", "new@mail.cl", "newpass", null, new Rol());

        when(userService.updateUserById(any(User.class), any())).thenReturn(updated);

        mockMvc.perform(patch("/api-v1/auth/update/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@mail.cl"));
    }
}
