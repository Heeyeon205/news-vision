package com.newsvision.user.controller;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.jwt.RefreshTokenRepository;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.user.dto.request.LoginUserRequest;
import com.newsvision.user.dto.response.LoginTokenUserResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import com.newsvision.user.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginTokenUserResponse>> login(@RequestBody LoginUserRequest request) {
        log.info("로그인 시도: {}", request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("ID or PW 불일치: {}", request.getPassword());
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        // token발급
        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

        // redis에 refresh token저장
        refreshTokenRepository.save(user.getUsername(), refreshToken);

        // access token은 프론트에 전달
        LoginTokenUserResponse response = new LoginTokenUserResponse(accessToken, refreshToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.split(" ")[1];

            if (jwtTokenProvider.validateToken(token)) {
                // access token 블랙리스트 등록
                long expiration = jwtTokenProvider.getExpiration(token);
                tokenBlacklistService.blacklistToken(token, expiration);

                // 식별자 추출 후 redis에서 refresh token 삭제
                String username = jwtTokenProvider.getUsername(token);
                refreshTokenRepository.delete(username);
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");

        // refresh token 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.fail(ErrorCode.INVALID_REFRESH_TOKEN));
        }

        // 유저 식별자 추출
        String username = jwtTokenProvider.getUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        // redis 에 저장된 refresh token 과 비교
        if (!refreshTokenRepository.exists(username, refreshToken)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.fail(ErrorCode.NOT_EXISTS_REFRESH_TOKEN));
        }

        // access token 추가 발급
        String newAccessToken = jwtTokenProvider.createToken(user.getId(), username, jwtTokenProvider.getUserRole(refreshToken));
        return ResponseEntity.ok(ApiResponse.success(newAccessToken));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<String>> checkAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null && !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(ApiResponse.fail(ErrorCode.INVALID_ACCESS_TOKEN));
        }

        String token = authHeader.split(" ")[1];

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.ok(ApiResponse.fail(ErrorCode.INVALID_ACCESS_TOKEN));
        }
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }
}
