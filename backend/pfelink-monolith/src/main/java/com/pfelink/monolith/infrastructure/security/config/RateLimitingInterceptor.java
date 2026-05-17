package com.pfelink.monolith.infrastructure.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final String RATE_LIMIT_KEY_PREFIX = "ratelimit:";
    private static final int AUTH_ENDPOINT_LIMIT = 5; // 5 attempts
    private static final long AUTH_ENDPOINT_WINDOW = 15 * 60; // 15 minutes in seconds

    private final RedisTemplate<String, Long> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        // Apply stricter rate limiting to auth endpoints
        if (isAuthEndpoint(path)) {
            String clientIp = getClientIp(request);
            String key = RATE_LIMIT_KEY_PREFIX + "auth:" + clientIp;

            Long attempts = redisTemplate.opsForValue().get(key);
            if (attempts == null) {
                attempts = 0L;
            }

            if (attempts >= AUTH_ENDPOINT_LIMIT) {
                log.warn("Rate limit exceeded for auth endpoint from IP: {}", clientIp);
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Too many login attempts. Please try again later.\"}");
                return false;
            }

            redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, AUTH_ENDPOINT_WINDOW, TimeUnit.SECONDS);
        }

        return true;
    }

    private boolean isAuthEndpoint(String path) {
        return path.startsWith("/api/auth/login") ||
               path.startsWith("/api/auth/register") ||
               path.startsWith("/api/auth/refresh");
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
