package com.pfelink.monolith.infrastructure.security.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    public void blacklistToken(String jti, Date expiryDate) {
        if (jti == null || expiryDate == null) return;
        
        long diff = expiryDate.getTime() - System.currentTimeMillis();
        if (diff > 0) {
            redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + jti,
                "true",
                Duration.ofMillis(diff)
            );
        }
    }

    public boolean isBlacklisted(String jti) {
        if (jti == null) return false;
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }
}
