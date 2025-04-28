package com.newsvision.global.auth;

import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.jwt.TempTokenRepository;
import com.newsvision.global.redis.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final TempTokenRepository tempTokenRepository;

    public void deleteTempToken(String tempToken) {
        if (jwtTokenProvider.validateToken(tempToken)) {
            long expiration = jwtTokenProvider.getExpiration(tempToken);
            tokenBlacklistService.blacklistToken(tempToken, expiration);
            String username = jwtTokenProvider.getUsername(tempToken);
            tempTokenRepository.delete(username);
        }
    }
}
