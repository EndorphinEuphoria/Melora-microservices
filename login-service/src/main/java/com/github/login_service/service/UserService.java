package com.github.login_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.github.login_service.model.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final String url_register_service = "http://localhost:8082/api-v1/register";

    // Manejar error al conseguir usuario por email, para evitar excepciones (error 500)
    // Conseguir objeto de usuario conectandose al microservicio de registro.
    // Esta forma sólo funciona si se tiene el objeto que se va a conseguir.
    public User userExists(String email) {
        User usuarioRequest = new User(); // Crea la request donde se guarda un objeto Usuario.
        usuarioRequest.setEmail(email); // agrega la información que se enviará para la request (email)
        
        String url = url_register_service + "/exists"; // url que devuelve un objeto Usuario
        
        try { // define los parámetros necesarios para el restTemplate (url, request con la info, respuesta de register)
            User usuarioResponse = restTemplate.postForObject(url, usuarioRequest, User.class);
            return usuarioResponse; // Devuelve el objeto de Usuario para ser utilizado después.
        } catch (HttpClientErrorException e) { // prevee errores del lado de register (al no encontrar al usuario)
            return null;

        } catch (Exception e) { // prevee cualquier otro error.
            throw new RuntimeException("Error al verificar existencia del usuario" + e.getMessage());
        } 
    }

    public boolean validateUser(String email, String password) {
        User usuario1 = userExists(email); 
        if (usuario1 == null) {
            return false;

        }
        
        if (passwordEncoder.matches(password, usuario1.getPassword())) {
        return true;
        
        }
        
        return false;
    }

    public String encrypt(String rawPasswd) {
        return passwordEncoder.encode(rawPasswd);
    }



}