package com.github.recoverpass_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.recoverpass_service.dto.UserRequest;
import com.github.recoverpass_service.dto.UserResponse;
import com.github.recoverpass_service.service.RecoverPassService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api-v1/recover")
@RequiredArgsConstructor
public class RecoverPassController {

    private final RecoverPassService recoverPassService;

    @GetMapping
    public ResponseEntity<UserResponse> recover(@RequestBody UserRequest userRequest) {
        UserResponse request = recoverPassService.validateUser(userRequest);
        if (request.getStatusCode() == 200) {
            return ResponseEntity.status(HttpStatus.OK).body(request);
        }
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(request);
    }
}
