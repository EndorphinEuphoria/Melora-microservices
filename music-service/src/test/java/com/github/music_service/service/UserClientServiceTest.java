package com.github.music_service.service;

import com.github.music_service.dto.UserResponseDto;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class UserClientServiceTest {

    @Mock private RestTemplate restTemplate;

    @InjectMocks
    private UserClientService userClientService;

    @Test
    void getNickname_returnsNickname() {
        UserResponseDto user = new UserResponseDto();
        user.setNickname("Test");

        when(restTemplate.getForObject(anyString(), eq(UserResponseDto.class)))
                .thenReturn(user);

        String result = userClientService.getNicknameByUserId(1L);

        assertEquals("Test", result);
    }

    @Test
    void getNickname_returnsDesconocidoOnError() {
        when(restTemplate.getForObject(anyString(), eq(UserResponseDto.class)))
                .thenThrow(new RuntimeException());

        String result = userClientService.getNicknameByUserId(1L);

        assertEquals("Desconocido", result);
    }
}
