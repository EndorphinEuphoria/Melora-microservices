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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api-v1/reset-password")
@RequiredArgsConstructor
public class RecoverPassController {

    private final ResetTokenRepository resetTokenRepository;
    private final RecoverPassService recoverPassService;

    @Operation(summary = "Solicita un token de reseteo de contraseña para un usuario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK: el correo se envió o el usuario no existe (por seguridad).",
                content = @Content(schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: el cuerpo de la petición es inválido.",
                content = @Content)
    })
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

    @Operation(summary = "Valida un token de reseteo de contraseña.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK: el token es válido.",
                content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: token inválido o expirado.",
                content = @Content)
    })
    @GetMapping("validateToken/{token}")
        public ResponseEntity<String> validateToken(@PathVariable String token) {
        Optional<ResetToken> tokenOpt = resetTokenRepository.findByToken(token); // Encuentra el token a través del token, el cuál contiene al usuario

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Token is valid");
    }

    @Operation(summary = "Resetea la contraseña de un usuario usando un token válido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK: la contraseña se actualizó correctamente.",
                content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST: token inválido o expirado.",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "NOT FOUND: usuario no encontrado.",
                content = @Content)
    })
    @PutMapping("reset/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        recoverPassService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }
}