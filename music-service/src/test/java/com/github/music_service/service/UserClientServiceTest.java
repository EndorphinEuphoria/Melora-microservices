package com.github.music_service.service;

import com.github.music_service.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class UserClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserClientService userClientService;

    @Test
    void getNicknameByUserId_returnsNickname() {
        UserResponseDto response = new UserResponseDto();
        response.setNickname("Lukas");

        when(restTemplate.getForObject(anyString(), eq(UserResponseDto.class)))
                .thenReturn(response);

        String nickname = userClientService.getNicknameByUserId(1L);

        assertThat(nickname).isEqualTo("Lukas");
    }

    @Test
    void getNicknameByUserId_returnsDesconocidoOnError() {
        when(restTemplate.getForObject(anyString(), eq(UserResponseDto.class)))
                .thenThrow(new RuntimeException("Error"));

        String nickname = userClientService.getNicknameByUserId(99L);

        assertThat(nickname).isEqualTo("Desconocido");
    }
}
