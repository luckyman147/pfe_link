package com.pfelink.monolith.infrastructure.security.config;

import com.pfelink.monolith.infrastructure.security.token.JwtService;
import com.pfelink.monolith.infrastructure.security.token.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractBearerToken(request);
            if (token != null && isTokenValidAndNotBlacklisted(token)) {
                authenticateFromToken(request, token);
            }
        } catch (Exception e) {
            log.debug("JWT filter error: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean isTokenValidAndNotBlacklisted(String token) {
        String jti = jwtService.extractJti(token);
        if (jti != null && blacklistService.isTokenBlacklisted(jti)) {
            log.debug("Rejected request with blacklisted token");
            return false;
        }
        return jwtService.isTokenValid(token);
    }

    private void authenticateFromToken(HttpServletRequest request, String token) {
        String email = jwtService.extractEmail(token);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (UsernameNotFoundException e) {
            log.debug("User not found for token: {}", email);
        }
    }
}
