package com.newsvision.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private Key key;

    // access token 30분
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 30;
    // refresh token 7일
    private static final long REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 7;

    @PostConstruct
    public void init() {
        // secret key 초기화 -> BASE64로 인코딩된 키를 디코딩하여 Key 객체 생성
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA256 키 생성
    }

    // access token 생성
    public String createToken(Long userId, String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .subject(username)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .claim("userId", userId)
                .claim("role", role)
                .signWith(key)
                .compact();
    }

    // refresh token 생성
    public String createRefreshToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);

        return Jwts.builder()
                .subject(username)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        // 서명 키 + 파싱 후 검증 프로세스
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    // 모든 token 에서 유저 id  조회
    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    // 모든 token 에서 유저 username  조회
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // access token 에서 유저 role 조회
    public String getUserRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    // 모든 token 에서 payload 파싱
    private Claims parseClaims(String token) {
        // subject, role, expiration 등 클레임에 접근
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 모든 token 만료 반환
    public long getExpiration(String token) {
        return parseClaims(token).getExpiration().getTime() - System.currentTimeMillis();
    }
}
