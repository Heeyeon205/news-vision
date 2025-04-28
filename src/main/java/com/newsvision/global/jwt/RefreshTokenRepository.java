package com.newsvision.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    // redis 에 토큰 저장
    public void save(String username, String refreshToken) {
        long expireTime = 60 * 60 * 24 * 7; // refresh token = 7일
        redisTemplate.opsForValue().set(username, refreshToken, Duration.ofSeconds(expireTime));
    }
    // 토큰 조회
    public String get(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    // 로그아웃 시 토큰 삭제
    public void delete(String username) {
        redisTemplate.delete(username);
    }

    // 클라이언트 요청 시 토큰 확인
    public boolean exists(String username) {
        return redisTemplate.hasKey(username);
    }

    // 저장된 refresh token 과 클라이언트가 보낸 토큰 비교
    public boolean exists(String username, String refreshToken) {
        String stored = get(username); // redis 에 저장된 토큰
        return stored != null && stored.equals(refreshToken);
    }
}
