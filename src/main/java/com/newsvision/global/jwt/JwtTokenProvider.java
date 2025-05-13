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
    // 임시 토큰 10분
    private static final long TEMP_TOKEN_VALIDITY = 1000 * 60 * 10;

    @PostConstruct
    public void init() {
        log.info("JWT secret from properties: {}", jwtProperties.getSecret());
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }catch (Exception e) {
            log.error("JWT 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    public String createTempToken(Long userId, String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + TEMP_TOKEN_VALIDITY);

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

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT: {}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Long getUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }
    public String getUserRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getExpiration(String token) {
        return parseClaims(token).getExpiration().getTime() - System.currentTimeMillis();
    }
}
