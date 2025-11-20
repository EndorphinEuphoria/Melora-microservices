package com.github.recoverpass_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.recoverpass_service.dto.UserRequest;
import com.github.recoverpass_service.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecoverPassService {

    private final RestTemplate restTemplate;
    private static final String USER_SERVICE_URL = "http://localhost:8082/api-v1/auth"; // TODO: cambiar url a la nueva api de registro para buscar usuario por email.

    public UserResponse validateUser(UserRequest userRequest) {
        try {
            UserRequest user = restTemplate.getForObject(
                USER_SERVICE_URL +
                 "/existsByEmail/" + userRequest.getEmail(),
                  UserRequest.class);

            if (user != null && user.getEmail() != null) {
                UserResponse userResponse = new UserResponse(200, "Revise su bandeja en su correo o el apartado de spam e ingrese el código en la app");
                return userResponse;
            }

        } catch (Exception e) {
            System.err.println("Error al consultar email de usuario.");
        }

        UserResponse userResponseInvalid = new UserResponse(204, "Email inválido o no ha sido registrado.");
        return userResponseInvalid;
    }
}
