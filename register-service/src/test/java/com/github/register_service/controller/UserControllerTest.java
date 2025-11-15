package com.github.register_service.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.register_service.dto.UserDto;
import com.github.register_service.model.User;
import com.github.register_service.model.Rol;
import com.github.register_service.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void getAllUsers_returnsOKJson() throws Exception {
        List<User> usuarios = Arrays.asList(
                new User(1L, "test1",  "test1@a.cl", "test1", null, null),
                new User(2L, "test2", "test2@a.cl", "test2", null, null));

        when(userService.findAllUsers()).thenReturn(usuarios);

        mockMvc.perform(get("/api-v1/register/getall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userList").isArray())
                .andExpect(jsonPath("$._embedded.userList[0].email").value("test1@a.cl"))
                .andExpect(jsonPath("$._embedded.userList[1].email").value("test2@a.cl"));
    }

    @Test
    void getUserbyID_returnsUser() throws Exception {
        Long idUser = 1L;
        User user = new User(idUser, "test2", "test2@a.cl", "test2", null, null);

        when(userService.findUserById(idUser)).thenReturn(user);

        mockMvc.perform(get("/api-v1/register/exists/{id}", idUser))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUser").value(1L));
    }

    @Test
    void getUserByEmail_returnsUser() throws Exception {
        User user = new User(1L, "test2", "test2@a.cl", "test2", null, null);

        when(userService.getByMail("test2@a.cl")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api-v1/register/getbymail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test2@a.cl\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test2@a.cl"));
    }

    @Test
    void deleteuserById_returnsok() throws Exception {
        Long idUser = 1L;

        doNothing().when(userService).deleteUserById(idUser);

        mockMvc.perform(delete("/api-v1/register/delete/{idUser}", idUser))
                .andExpect(status().isOk());
    }

    @Test
    void createUserTest_returnsCreatedUser() throws Exception {
        UserDto dto = new UserDto();
        dto.setNickname("nick");
        dto.setEmail("test2@a.cl");
        dto.setPassword("pass");
        dto.setRolId(1L);
        dto.setProfilePhotoBase64(null);

        User saved = new User(1L,  "nick", "test2@a.cl", "pass", null, new Rol());

        when(userService.getByMail(dto.getEmail())).thenReturn(Optional.empty());
        when(userService.saveUser(any(User.class), any())).thenReturn(saved);

        mockMvc.perform(post("/api-v1/register/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario.email").value("test2@a.cl"));
    }

    @Test
    void updateusuarioByID_returnsUpdatedUsers() throws Exception {
        Long idUser = 10L;

        UserDto dto = new UserDto();
        dto.setEmail("nuevo@mail.com");
        dto.setNickname("nick2");
        dto.setPassword("pass2");
        dto.setRolId(1L);
        dto.setProfilePhotoBase64(null);

        User updated = new User(idUser,  "nick2", "nuevo@mail.com", "pass2", null, new Rol());

        when(userService.updateUserById(any(User.class), any())).thenReturn(updated);

        mockMvc.perform(patch("/api-v1/register/update/{idUser}", idUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("nuevo@mail.com"));
    }
}
