package com.github.music_service.controller;

import com.github.music_service.dto.UploadRequestDto;
import com.github.music_service.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api-v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/full")
    public ResponseEntity<Map<String, Long>> uploadSong(@RequestBody UploadRequestDto dto) {
        Map<String, Long> response = uploadService.uploadSong(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}