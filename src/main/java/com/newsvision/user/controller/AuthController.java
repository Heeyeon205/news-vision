package com.newsvision.user.controller;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.jwt.RefreshTokenRepository;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.jwt.TempTokenRepository;
import com.newsvision.user.dto.request.LoginUserRequest;
import com.newsvision.user.dto.request.VerifyEmailRequest;
import com.newsvision.user.dto.response.LoginTokenUserResponse;
import com.newsvision.user.dto.response.TempTokenResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.EmailService;
import com.newsvision.user.service.TokenBlacklistService;
import com.newsvision.user.service.UserService;
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
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final TempTokenRepository tempTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginTokenUserResponse>> login(@RequestBody LoginUserRequest request) {
        log.info("로그인 시도: {}", request.getUsername());
        User user = userService.findByUsername(request.getUsername());

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
        LoginTokenUserResponse response = new LoginTokenUserResponse(accessToken, refreshToken, user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
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
        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
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
        User user = userService.findByUsername(username);

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

    @PostMapping("/email-auth")
    public ResponseEntity<ApiResponse<?>> emailAuth(@RequestBody VerifyEmailRequest request) {
        emailService.verifyCode(request.getEmail(), request.getEmailCode());
        User user = userService.findByEmail(request.getEmail());

        String tempToken = jwtTokenProvider.createTempToken(user.getId(), user.getUsername(), user.getRole().name());

        // redis에 임시 토큰 저장
        tempTokenRepository.save(user.getUsername(), tempToken);

        // 임시 토큰 전달
        TempTokenResponse response = new TempTokenResponse(user.getUsername(), tempToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/temp-check")
    public ResponseEntity<ApiResponse<String>> checkTempToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(ApiResponse.fail(ErrorCode.INVALID_ACCESS_TOKEN));
        }

        String tempToken = authHeader.split(" ")[1];
        log.info("tempToken: {}", tempToken);

        if (!jwtTokenProvider.validateToken(tempToken)) {
            return ResponseEntity.ok(ApiResponse.fail(ErrorCode.INVALID_ACCESS_TOKEN));
        }
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    public void deleteTempToken(String tempToken) {
        log.info("tempToken: {}", tempToken);
            if (jwtTokenProvider.validateToken(tempToken)) {
                long expiration = jwtTokenProvider.getExpiration(tempToken);
                tokenBlacklistService.blacklistToken(tempToken, expiration);
                String username = jwtTokenProvider.getUsername(tempToken);
                refreshTokenRepository.delete(username);
            }
        log.info("검증: {}", tempToken == null ? "null" : tempToken);
        log.info("삭제됨?");
        }
}
