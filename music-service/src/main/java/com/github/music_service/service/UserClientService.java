package com.github.music_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.music_service.dto.UserResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserClientService {
    
    private final RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8082/api-v1/register";

    public String getNicknameByUserId(Long userId) {
        try {
            UserResponseDto user = restTemplate.getForObject(
                USER_SERVICE_URL + "/exists/" + userId, 
                UserResponseDto.class
            );

            if (user != null && user.getNickname() != null) {
                return user.getNickname();
            }
        } catch (Exception e) {
            System.err.println(" Error al consultar nickname de usuario ID " 
                + userId + ": " + e.getMessage());
        }

        return "Desconocido";
    }

}
