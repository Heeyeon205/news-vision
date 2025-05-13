package com.newsvision.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
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
        boolean result = redisTemplate.hasKey(TOKEN_PREFIX + token);
        return result;
    }
}
