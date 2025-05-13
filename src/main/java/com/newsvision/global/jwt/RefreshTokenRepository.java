package com.newsvision.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void save(String username, String refreshToken) {
        long expireTime = 60 * 60 * 24 * 7;
        redisTemplate.opsForValue().set(username, refreshToken, Duration.ofSeconds(expireTime));
    }

    public String get(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    public void delete(String username) {
        redisTemplate.delete(username);
    }

    public boolean exists(String username, String refreshToken) {
        String stored = get(username);
        return stored != null && stored.equals(refreshToken);
    }
}
