package com.github.register_service.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.register_service.model.User;
import com.github.register_service.service.UserService;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    //Aqui probarsemos si el metodo alojado en services trae una lista con todos los usuarios
    @Test
    void getAllUsers_returnsOKJson() throws Exception{
        List<User> usuarios = Arrays.asList(new User(1L, "test1", "test1", "test1", "test1@a.cl", "test1", "test1", null),
        new User(1L, "test2", "test2", "test2", "test2@a.cl", "test2", "test2", null));

        when(userService.findAllUsers()).thenReturn(usuarios);

        mockMvc.perform(get("/api-v1/register/getall"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.userList").isArray())
        .andExpect(jsonPath("$._embedded.userList[0].email").value("test1@a.cl"))
        .andExpect(jsonPath("$._embedded.userList[1].email").value("test2@a.cl"));
    }

    //Aqui probaremos si el metodo para listar usuarios por id trae al usuario correctamente
    @Test
    void getUserbyID_returnsUser(){
       Long idUser = 1L;

       User user =  new User(idUser, "test2", "test2", "test2", "test2@a.cl", "test2", "test2", null);

       when(userService.findUserById(idUser)).thenReturn(user);

       try {
        mockMvc.perform(get("/api-v1/register/exists/{id}",idUser)).andExpect(status().isOk()).andExpect(jsonPath("$.idUser").value(1L));
       } catch (Exception e) {
            e.printStackTrace();
       }
    }

    //Aqui probaremos si el metodo para obtener usuarios por email trae al usuario correctamente
    @Test
    void getUserByEmail_returnsUser(){

        User user =  new User(1L, "test2", "test2", "test2", "test2@a.cl", "test2", "test2", null);

        when(userService.getByMail("test2@a.cl")).thenReturn(Optional.of(user));

        try {
            mockMvc.perform(post("/api-v1/register/getbymail").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"test2@a.cl\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.email").
            value("test2@a.cl")); //usamos el mediaType y contentType para que reconozca que el email viene por un json
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // TODO: Arreglar test para que de 200 en vez de 500
    @Test
    void deleteuserById_returnsok() throws Exception {
        Long idUser = 1L;
        doNothing().when(userService).deleteUserById(idUser);

        String jsonResponse = mockMvc.perform(delete("/api-v1/register/delete/{idUser}", idUser))
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();

        // assertTrue(jsonResponse.get("content").toString().contains("El usuario ha sido borrado con éxito."));
    }



    //Aqui probaremos si el metood para crear usuarios devuelve un codigo 201 CREATED 
    @Test
    void createUserTest_returnsCreatedUseriscreated() throws Exception{
        User user =  new User(1L, "test2", "test2", "test2", "test2@a.cl", "test2", "test2", null);
        
        when(userService.getByMail(user.getEmail())).thenReturn(null);

        when(userService.saveUser(any(User.class))).thenReturn(user);

        ObjectMapper objectMapper = new ObjectMapper();
        String Json = objectMapper.writeValueAsString(user);

        try {
            mockMvc.perform(post("/api-v1/register").contentType(MediaType.APPLICATION_JSON).content(Json)).andExpect(status().isCreated()).
            andExpect(content().string("Usuario creado correctamente."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Aqui probaremos si el metodo para actualizar usuarios actualiza de forma correcta el email de el usuario y de ser asi lanza un codigo OK
    @Test
    void updateusuarioByID_returnsUpdatedUsers(){
        Long idUser = 10L;
        
        User user =  new User(idUser, "test1", "test1","test1", "test1@a.cl", "test1", "test1", null);

        User user2 =  new User(idUser,"test2", "test2", "test2", "test2@a.cl", "test2", "test2", null);

        when(userService.updateUserById(any(User.class))).thenReturn(user2);

        try {
            mockMvc.perform(patch("/api-v1/register/update/{idUser}", idUser).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper()
            .writeValueAsString(user))).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("test2@a.cl"));       
         } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
