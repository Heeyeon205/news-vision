package com.newsvision.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final StringRedisTemplate redisTemplate;
    private static final String TOKEN_PREFIX = "token_";

    public void blacklistToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(
                TOKEN_PREFIX + token,
                "logout",
                expirationMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(TOKEN_PREFIX + token);
    }
}
