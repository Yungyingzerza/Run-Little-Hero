package com.yungying.servergame.SERVER.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION}")
    private long jwtExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        try {
            // Ensure the secret key is long enough (at least 64 bytes for HS512)
            if (jwtSecret.length() < 64) {
                throw new IllegalArgumentException("JWT secret must be at least 64 characters long.");
            }

            // Initialize the secret key from the jwtSecret
            this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        } catch (Exception e) {
            // Log the error message
            e.printStackTrace(); // Log the exception for debugging
            throw new RuntimeException("Failed to initialize JWT Utils", e);
        }
    }

    // Generate a JWT token
    public String generateJwtToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000)) // Convert seconds to milliseconds
                .signWith(secretKey, SignatureAlgorithm.HS512) // Use the generated secure key
                .compact();
    }

    // Validate a JWT token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token); // Use parseClaimsJws if available for validation
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get username from JWT token
    public String getUserIdFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token) // This should be used to extract claims
                .getBody();
        return claims.getSubject();
    }
}
