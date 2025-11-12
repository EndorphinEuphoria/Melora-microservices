package com.github.login_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.login_service.model.User;
import com.github.login_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api-v1")
@RequiredArgsConstructor
public class UsuarioController {

    private final UserService userService;

    @Operation(summary = "Este endpoint permite validar usuario al iniciar sesión")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202", 
            description = "Usuario validado correctamente",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Usuario ingresado con éxito: usuario@gmail.com"))),

        @ApiResponse(
            responseCode = "400", 
            description = "Email o contraseña incorrectos")
    })
    @PostMapping("/login")
    public ResponseEntity<?> validateUsuario(@RequestBody User user) {
        try {
            if(userService.validateUser(user.getEmail(), user.getPassword())){

                Map<String, Object> response = new LinkedHashMap<>();

                response.put("self", "http://localhost:8083/api-v1/login");
                return ResponseEntity.accepted().body(response);

            }
            return ResponseEntity.badRequest().body("Error: Email o contraseña incorrectas");
        } catch (Exception e) {
            throw new RuntimeException("Error al autenticar." + e.getMessage());
        }
    }
    
    
}
