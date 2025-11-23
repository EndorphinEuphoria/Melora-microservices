package com.github.recoverpass_service.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.recoverpass_service.model.ResetToken;
import com.github.recoverpass_service.model.User;
import com.github.recoverpass_service.repository.ResetTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecoverPassService {

    private final RestTemplate restTemplate;
    private final JavaMailSender mailSender;
    private final ResetTokenRepository resetTokenRepository;
    private static final String USER_SERVICE_URL = "http://localhost:8082/api-v1/auth"; // TODO: cambiar url a la nueva api de registro para buscar usuario por email.
    private static final SecureRandom secureRandom = new SecureRandom();

    // Validar usuario
    public User validateUser(String email) {
        try {

            Map<String, String> body = Map.of("email", email);
            User user1 = restTemplate.postForObject(
            USER_SERVICE_URL + "/getbymail",
            body,
            User.class);

        return user1;

        } catch (Exception e) {
            return null;
        }
    }


    // create reset-password token
    public String makeResetToken() {
        int code = secureRandom.nextInt(900000) + 100000; // 6 dígitos
        return String.valueOf(code);
    }


    // send email
    public void sendEmail(String email, String token) {
        String body = "Copy this code into the application to reset your password.\n" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password reset");
        message.setText(body);

        mailSender.send(message);
    }
    
    // Process request 
    public boolean processRequest(String email) {
        User user = validateUser(email);
        
        if (user == null) return false;

        String token = makeResetToken();
        
        ResetToken record = new ResetToken();
        record.setToken(token);
        record.setUserId(user.getIdUser());
        record.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        resetTokenRepository.save(record);

        sendEmail(user.getEmail(), token);
        return true;
    }

    public void resetPassword(String token, String newPassword) {
        ResetToken resetToken = resetTokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.isExpired() || resetToken.isUsed()) {
            throw new RuntimeException("Token expired or already used");
        }

        restTemplate.put(
            USER_SERVICE_URL + "/resetPassword/" + resetToken.getUserId(),
            Map.of("newPassword", newPassword)
        );

        // Marcar token como usado
        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
    }
}