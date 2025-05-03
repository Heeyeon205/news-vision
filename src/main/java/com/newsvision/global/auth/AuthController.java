package com.newsvision.global.auth;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.jwt.JwtUtil;
import com.newsvision.global.jwt.RefreshTokenRepository;
import com.newsvision.global.jwt.TempTokenRepository;
import com.newsvision.global.redis.TokenBlacklistService;
import com.newsvision.user.dto.request.LoginUserRequest;
import com.newsvision.user.dto.request.VerifyEmailRequest;
import com.newsvision.user.dto.response.LoginTokenUserResponse;
import com.newsvision.user.dto.response.TempTokenResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.EmailService;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "인증 컨트롤러", description = "유저 로그인/로그아웃 및 토큰 관련 API")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TempTokenRepository tempTokenRepository;

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인하고 Access, Refresh Token을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginTokenUserResponse>> login(@RequestBody LoginUserRequest request) {
        User user = userService.findByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("ID or PW 불일치: {}", request.getPassword());
            throw new CustomException(ErrorCode.INVALID_LOGIN_INFO);
        }

        String accessToken = jwtTokenProvider.createToken(user.getId(), user.getUsername(), user.getRole().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());
        refreshTokenRepository.save(user.getUsername(), refreshToken);

        LoginTokenUserResponse response = new LoginTokenUserResponse(
                accessToken, refreshToken, user.getId(),
                user.getNickname(), user.getImage(), user.getRole().name()
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "로그아웃", description = "Refresh Token을 블랙리스트에 등록하고 Redis에서 삭제합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        String refreshToken = JwtUtil.parsingToken(request.getHeader("Authorization"));

        if (jwtTokenProvider.validateToken(refreshToken)) {
            long expiration = jwtTokenProvider.getExpiration(refreshToken);
            tokenBlacklistService.blacklistToken(refreshToken, expiration);
            String username = jwtTokenProvider.getUsername(refreshToken);
            refreshTokenRepository.delete(username);
        }

        return ResponseEntity.ok(ApiResponse.success("로그아웃 성공"));
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token을 이용해 Access Token을 재발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body(ApiResponse.fail(ErrorCode.INVALID_REFRESH_TOKEN));
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        User user = userService.findByUsername(username);

        if (!refreshTokenRepository.exists(username, refreshToken)) {
            return ResponseEntity.status(401).body(ApiResponse.fail(ErrorCode.NOT_EXISTS_REFRESH_TOKEN));
        }

        String newAccessToken = jwtTokenProvider.createToken(
                user.getId(), username, jwtTokenProvider.getUserRole(refreshToken)
        );
        return ResponseEntity.ok(ApiResponse.success(newAccessToken));
    }

    @Operation(summary = "Access Token 유효성 확인", description = "Authorization 헤더에 포함된 Access Token의 유효성을 검사합니다.")
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<String>> checkAccessToken(HttpServletRequest request) {
        String accessToken = JwtUtil.parsingToken(request.getHeader("Authorization"));

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @Operation(summary = "이메일 인증", description = "이메일과 인증 코드를 확인하고 임시 토큰을 발급합니다.")
    @PostMapping("/email-auth")
    public ResponseEntity<ApiResponse<TempTokenResponse>> emailAuth(@RequestBody VerifyEmailRequest request) {
        emailService.verifyCode(request.getEmail(), request.getEmailCode());
        User user = userService.findByEmail(request.getEmail());

        String tempToken = jwtTokenProvider.createTempToken(user.getId(), user.getUsername(), user.getRole().name());
        tempTokenRepository.save(user.getUsername(), tempToken);

        TempTokenResponse response = new TempTokenResponse(user.getUsername(), tempToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "임시 토큰 확인", description = "Authorization 헤더에 포함된 임시 토큰의 유효성을 검사합니다.")
    @GetMapping("/temp-check")
    public ResponseEntity<ApiResponse<String>> checkTempToken(HttpServletRequest request) {
        String tempToken = JwtUtil.parsingToken(request.getHeader("Authorization"));

        if (!jwtTokenProvider.validateToken(tempToken)) {
            throw new CustomException(ErrorCode.INVALID_TEMP_TOKEN);
        }
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }
}
