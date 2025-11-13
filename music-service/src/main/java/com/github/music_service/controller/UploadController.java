package com.github.music_service.controller;

import com.github.music_service.dto.UploadRequestDto;
import com.github.music_service.service.UploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api-v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @Operation(
            summary = "Subir canción completa",
            description = "Crea una canción y su registro de subida en un solo endpoint."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Canción subida correctamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario inexistente",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/full")
    public ResponseEntity<?> uploadSong(@RequestBody UploadRequestDto dto) {
        try {
            Map<String, Long> response = uploadService.uploadSong(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "No se pudo subir la canción: " + e.getMessage()));
        }
    } 
}