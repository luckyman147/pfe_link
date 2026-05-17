package com.pfelink.monolith.infrastructure.security.token;

import com.pfelink.monolith.domain.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenBuilder tokenBuilder;

    @Value("${jwt.access-token-expiration:900000}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpiration;

    public String generateToken(User user) {
        return generateToken(user, accessTokenExpiration, false);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, refreshTokenExpiration, true);
    }

    private String generateToken(User user, long expiration, boolean isRefreshToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("jti", UUID.randomUUID().toString());
        if (isRefreshToken) {
            claims.put("type", "refresh");
        }
        return tokenBuilder.buildToken(claims, user.getEmail(), expiration);
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractJti(String token) {
        return extractAllClaims(token).get("jti", String.class);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(tokenBuilder.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
