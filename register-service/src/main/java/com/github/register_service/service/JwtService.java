package com.github.register_service.service;

import java.util.List;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;

import com.github.register_service.model.Rol;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {
    
    String generateToken(String email, List<Rol> rol);
    Authentication getAuthentication(String token);
    String getUsername(String token);
    String resolveToken(HttpServletRequest req);
    boolean validateToken(String token);
}
