package com.newsvision.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class TempTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public void save(String username, String tempToken) {
        long expireTime = 1000 * 60 * 10;
        redisTemplate.opsForValue().set(username, tempToken, Duration.ofSeconds(expireTime));
    }

    public String get(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    public void delete(String username) {
        redisTemplate.delete(username);
    }

    public boolean exists(String username) {
        return redisTemplate.hasKey(username);
    }

    public boolean exists(String username, String tempToken) {
        String stored = get(username);
        return stored != null && stored.equals(tempToken);
    }
}
