package com.example.spring_security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import com.example.spring_security.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String jwtsecretkey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtsecretkey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {

        String token = Jwts.builder().subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", Set.of("ADMIN", "USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 10000 * 69))
                .signWith(getSecretKey()).compact();
        return token;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSecretKey())
                .build().parseSignedClaims(token).getPayload();

        return Long.valueOf(claims.getSubject());
    }

    public Long extractUserId(String token) {
        return getUserIdFromToken(token);
    }
}
