package com.github.login_service.service;

import com.github.login_service.model.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    // userExists - Usuario encontrado
    @Test
    void userExists_returnsUser() {

        User requestUser = new User();
        requestUser.setEmail("test@mail.com");

        User responseUser = new User();
        responseUser.setEmail("test@mail.com");
        responseUser.setPassword("1234");

        when(restTemplate.postForObject(
                contains("/exists"),
                any(User.class),
                eq(User.class)))
        .thenReturn(responseUser);

        User result = userService.userExists("test@mail.com");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@mail.com");
    }

    // userExists - Usuario no encontrado (404)
    @Test
    void userExists_notFound_returnsNull() {

        when(restTemplate.postForObject(
                contains("/exists"),
                any(User.class),
                eq(User.class)))
        .thenThrow(HttpClientErrorException.NotFound.class);

        User result = userService.userExists("no@found.com");

        assertThat(result).isNull();
    }

    // userExists - Otro error 
    @Test
    void userExists_throwsRuntimeOnOtherError() {

        when(restTemplate.postForObject(anyString(), any(), eq(User.class)))
                .thenThrow(new RuntimeException("Server error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.userExists("error@mail.com")
        );

        assertThat(exception.getMessage())
                .contains("Error al verificar existencia del usuario");
    }

    // validateUser - contraseña correcta
    @Test
    void validateUser_returnsTrue_whenPasswordMatches() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encodedPass");

        when(restTemplate.postForObject(anyString(), any(), eq(User.class)))
                .thenReturn(user);

        when(passwordEncoder.matches("1234", "encodedPass"))
                .thenReturn(true);

        boolean result = userService.validateUser("test@mail.com", "1234");

        assertThat(result).isTrue();
    }

    // validateUser - contraseña incorrecta
    @Test
    void validateUser_returnsFalse_whenPasswordDoesNotMatch() {

        User user = new User();
        user.setPassword("encodedPass");

        when(restTemplate.postForObject(anyString(), any(), eq(User.class)))
                .thenReturn(user);

        when(passwordEncoder.matches("wrong", "encodedPass"))
                .thenReturn(false);

        boolean result = userService.validateUser("a@mail.com", "wrong");

        assertThat(result).isFalse();
    }

    // validateUser - usuario inexistente 
    @Test
    void validateUser_returnsFalse_whenUserDoesNotExist() {

        when(restTemplate.postForObject(anyString(), any(), eq(User.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        boolean result = userService.validateUser("x@mail.com", "pass");

        assertThat(result).isFalse();
    }

    // encrypt
    @Test
    void encrypt_returnsEncodedPassword() {
        when(passwordEncoder.encode("1234"))
                .thenReturn("ENCODED");

        String result = userService.encrypt("1234");

        assertThat(result).isEqualTo("ENCODED");
    }
}
