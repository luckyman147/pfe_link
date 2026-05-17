package com.pfelink.monolith.infrastructure.security.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private static final String BLACKLIST_KEY_PREFIX = "token:blacklist:";
    private final RedisTemplate<String, Boolean> redisTemplate;

    public void blacklistToken(String jti, Date expirationDate) {
        if (jti == null || expirationDate == null) {
            return;
        }
        String key = BLACKLIST_KEY_PREFIX + jti;
        long ttlSeconds = (expirationDate.getTime() - System.currentTimeMillis()) / 1000;
        if (ttlSeconds > 0) {
            redisTemplate.opsForValue().set(key, true, ttlSeconds, TimeUnit.SECONDS);
        }
    }

    public boolean isTokenBlacklisted(String jti) {
        if (jti == null) {
            return false;
        }
        String key = BLACKLIST_KEY_PREFIX + jti;
        Boolean isBlacklisted = redisTemplate.opsForValue().get(key);
        return isBlacklisted != null && isBlacklisted;
    }
}
