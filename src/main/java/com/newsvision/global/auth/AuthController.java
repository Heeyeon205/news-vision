package com.newsvision.global.auth;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.jwt.JwtUtil;
import com.newsvision.global.jwt.RefreshTokenRepository;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.jwt.TempTokenRepository;
import com.newsvision.user.dto.request.LoginUserRequest;
import com.newsvision.user.dto.request.VerifyEmailRequest;
import com.newsvision.user.dto.response.LoginTokenUserResponse;
import com.newsvision.user.dto.response.TempTokenResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.EmailService;
import com.newsvision.global.redis.TokenBlacklistService;
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
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    private final RefreshTokenRepository refreshTokenRepository;
    private final TempTokenRepository tempTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginTokenUserResponse>> login(@RequestBody LoginUserRequest request) {
        User user = userService.findByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("ID or PW 불일치: {}", request.getPassword());
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());
        refreshTokenRepository.save(user.getUsername(), refreshToken);

        LoginTokenUserResponse response = new LoginTokenUserResponse(accessToken, refreshToken, user.getUsername());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        String refreshToken = JwtUtil.extractToken(request.getHeader("Authorization"));

            if (jwtTokenProvider.validateToken(refreshToken)) {
                long expiration = jwtTokenProvider.getExpiration(refreshToken);
                tokenBlacklistService.blacklistToken(refreshToken, expiration);

                String username = jwtTokenProvider.getUsername(refreshToken);
                refreshTokenRepository.delete(username);
            }
        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.fail(ErrorCode.INVALID_REFRESH_TOKEN));
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        User user = userService.findByUsername(username);

        if (!refreshTokenRepository.exists(username, refreshToken)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.fail(ErrorCode.NOT_EXISTS_REFRESH_TOKEN));
        }

        String newAccessToken = jwtTokenProvider.createToken(user.getId(), username, jwtTokenProvider.getUserRole(refreshToken));
        return ResponseEntity.ok(ApiResponse.success(newAccessToken));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<String>> checkAccessToken(HttpServletRequest request) {
        String accessToken = JwtUtil.extractToken(request.getHeader("Authorization"));

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @PostMapping("/email-auth")
    public ResponseEntity<ApiResponse<?>> emailAuth(@RequestBody VerifyEmailRequest request) {
        emailService.verifyCode(request.getEmail(), request.getEmailCode());
        User user = userService.findByEmail(request.getEmail());

        String tempToken = jwtTokenProvider.createTempToken(user.getId(), user.getUsername(), user.getRole().name());
        tempTokenRepository.save(user.getUsername(), tempToken);

        TempTokenResponse response = new TempTokenResponse(user.getUsername(), tempToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/temp-check")
    public ResponseEntity<ApiResponse<String>> checkTempToken(HttpServletRequest request) {
        String tempToken = JwtUtil.extractToken(request.getHeader("Authorization"));

        if (!jwtTokenProvider.validateToken(tempToken)) {
           throw new CustomException(ErrorCode.INVALID_TEMP_TOKEN);
        }
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }
}
