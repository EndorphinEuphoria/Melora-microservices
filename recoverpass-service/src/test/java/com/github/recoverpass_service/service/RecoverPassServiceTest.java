package com.github.recoverpass_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import com.github.recoverpass_service.model.ResetToken;
import com.github.recoverpass_service.model.User;
import com.github.recoverpass_service.repository.ResetTokenRepository;
import com.github.recoverpass_service.service.RecoverPassService;

@ExtendWith(MockitoExtension.class)
public class RecoverPassServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private ResetTokenRepository resetTokenRepository;

    @InjectMocks
    private RecoverPassService recoverPassService;

    @Test
    void validateUser_returnsUserWhenExists() {
        User user = new User();
        user.setIdUser(1L);
        user.setEmail("test@a.cl");

        when(restTemplate.postForObject(
                any(String.class),
                any(Map.class),
                eq(User.class)
        )).thenReturn(user);

        User result = recoverPassService.validateUser("test@a.cl");

        assertThat(result).isEqualTo(user);
    }

    @Test
    void validateUser_returnsNullWhenException() {
        when(restTemplate.postForObject(
                any(String.class),
                any(Map.class),
                eq(User.class)
        )).thenThrow(new RuntimeException());

        User result = recoverPassService.validateUser("nonexistent@a.cl");

        assertThat(result).isNull();
    }

    @Test
    void makeResetToken_generates6DigitCode() {
        String token = recoverPassService.makeResetToken();
        assertThat(token).hasSize(6);
    }

    @Test
    void processRequest_savesTokenAndSendsEmail() {
        User user = new User();
        user.setIdUser(1L);
        user.setEmail("test@a.cl");

        RecoverPassService spyService = spy(recoverPassService);

        doReturn(user).when(spyService).validateUser("test@a.cl");
        doReturn("123456").when(spyService).makeResetToken();
        doNothing().when(spyService).sendEmail(anyString(), anyString());

        boolean result = spyService.processRequest("test@a.cl");

        assertThat(result).isTrue();
        verify(resetTokenRepository).save(any(ResetToken.class));
        verify(spyService).sendEmail("test@a.cl", "123456");
    }

    @Test
    void processRequest_returnsFalseIfUserInvalid() {
        RecoverPassService spyService = spy(recoverPassService);
        doReturn(null).when(spyService).validateUser("invalid@a.cl");

        boolean result = spyService.processRequest("invalid@a.cl");

        assertThat(result).isFalse();
        verify(resetTokenRepository, never()).save(any());
        verify(spyService, never()).sendEmail(anyString(), anyString());
    }

    @Test
    void resetPassword_worksCorrectly() {
        ResetToken token = new ResetToken();
        token.setToken("token123");
        token.setUserId(1L);
        token.setUsed(false);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        when(resetTokenRepository.findByToken("token123")).thenReturn(Optional.of(token));
        doNothing().when(restTemplate).put(anyString(), any(Map.class));
        when(resetTokenRepository.save(any(ResetToken.class))).thenReturn(token);

        recoverPassService.resetPassword("token123", "newPass");

        assertThat(token.isUsed()).isTrue();
        verify(restTemplate).put(contains("/resetPassword/1"), any(Map.class));
        verify(resetTokenRepository).save(token);
    }

    @Test
    void resetPassword_throwsIfTokenInvalid() {
        when(resetTokenRepository.findByToken("badToken")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                recoverPassService.resetPassword("badToken", "newPass")
        );
    }

    @Test
    void resetPassword_throwsIfTokenExpiredOrUsed() {
        ResetToken token = new ResetToken();
        token.setToken("token123");
        token.setUsed(true); // Already used
        token.setExpiresAt(LocalDateTime.now().minusMinutes(1)); // Expired

        when(resetTokenRepository.findByToken("token123")).thenReturn(Optional.of(token));

        assertThrows(RuntimeException.class, () ->
                recoverPassService.resetPassword("token123", "newPass")
        );
    }
}
