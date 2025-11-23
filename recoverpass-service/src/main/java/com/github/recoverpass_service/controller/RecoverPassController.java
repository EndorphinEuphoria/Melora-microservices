package com.github.recoverpass_service.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.recoverpass_service.model.ResetToken;
import com.github.recoverpass_service.model.User;
import com.github.recoverpass_service.repository.ResetTokenRepository;
import com.github.recoverpass_service.service.RecoverPassService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api-v1/reset-password")
@RequiredArgsConstructor
public class RecoverPassController {

    private final ResetTokenRepository resetTokenRepository;
    private final RecoverPassService recoverPassService;

    // Le envía el mail al usuario con el token generado, si es que existe
    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> requestReset(@RequestBody User user) {
        String email = user.getEmail();
        boolean ok = recoverPassService.processRequest(email);
        if (!ok) {                         // status ok por seguridad
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "message", "User not found",
                "status", true
            ));
        }
        return ResponseEntity.ok(Map.of(
            "message", "A reset link has been sent.",
            "status", true
        ));
    }

    @GetMapping("validateToken/{token}")
        public ResponseEntity<String> validateToken(@PathVariable String token) {
        Optional<ResetToken> tokenOpt = resetTokenRepository.findByToken(token); // Encuentra el token a través del token, el cuál contiene al usuario

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Token is valid");
    }

    @PutMapping("resetPassword/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        recoverPassService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }
}