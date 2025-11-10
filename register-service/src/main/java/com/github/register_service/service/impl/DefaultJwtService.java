package com.github.register_service.service.impl;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.github.register_service.model.Rol;
import com.github.register_service.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;


@Component
public class DefaultJwtService implements JwtService {

    @Value("${jwt.secret}")
    private SecretKey SECRET_KEY;

    @Value("${jwt.expiration}")
    private long expirationTimeInMillis;

    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    @Override
    public String generateToken(String email, List<Rol> rol) {
        
        Claims claims = Jwts.claims().subject(email).build();
        claims.put("auth", rol.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTimeInMillis);

        return Jwts.builder()
            .claims(claims)
            .issuedAt(now)
            .expiration(validity)
            .signWith(SECRET_KEY)
            .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = 
    }

    @Override
    public String getUsername(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
    }

    @Override
    public String resolveToken(HttpServletRequest req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resolveToken'");
    }

    @Override
    public boolean validateToken(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validateToken'");
    }
}
