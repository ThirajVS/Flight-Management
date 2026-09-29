package com.aerocadet.portal.auth;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.expiration-ms}") long expirationMs) {
        if (secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET must contain at least 32 characters");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public TokenDetails createToken(UserDetails userDetails) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(expirationMs);
        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
        return new TokenDetails(token, expiresAt);
    }

    public String extractSubject(String token) {
        return claims(token).getSubject();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        Claims claims = claims(token);
        return claims.getSubject().equalsIgnoreCase(userDetails.getUsername())
                && claims.getExpiration().after(new Date());
    }

    private Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public record TokenDetails(String value, Instant expiresAt) {
    }
}

