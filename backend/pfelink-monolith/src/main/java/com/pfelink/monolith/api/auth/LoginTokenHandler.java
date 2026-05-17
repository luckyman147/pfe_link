package com.pfelink.monolith.api.auth;

import com.pfelink.monolith.infrastructure.security.token.JwtService;
import com.pfelink.monolith.infrastructure.security.token.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginTokenHandler {

    private final JwtService jwtService;
    private final TokenBlacklistService blacklistService;

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void blacklistCurrentToken(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && jwtService.isTokenValid(token)) {
            blacklistService.blacklistToken(
                jwtService.extractJti(token),
                jwtService.extractExpiration(token)
            );
        }
    }
}
