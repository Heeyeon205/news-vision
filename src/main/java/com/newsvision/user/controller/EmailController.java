package com.newsvision.user.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.user.dto.request.EmailRequest;
import com.newsvision.user.dto.request.VerifyEmailRequest;
import com.newsvision.user.dto.response.CheckUserUsernameResponse;
import com.newsvision.user.service.EmailService;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Tag(name = "이메일 인증 컨트롤러", description = "이메일 인증 및 코드 검증 API")
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    @Operation(
            summary = "이메일 인증코드 전송",
            description = "입력된 이메일로 인증코드를 전송하며, 이미 가입된 이메일인지 여부를 함께 확인합니다."
    )
    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<?>> sendCode(@RequestBody EmailRequest request) {
        Boolean exists = userService.existsByEmail(request.getEmail());
        if (exists){
            return ResponseEntity.ok(ApiResponse.success(new CheckUserUsernameResponse(exists)));
        }
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @Operation(
            summary = "이메일 인증코드 검증",
            description = "입력된 이메일과 인증코드를 비교하여 일치 여부를 검증합니다."
    )
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyEmailRequest>> verifyCode(@RequestBody VerifyEmailRequest request) {
        emailService.verifyCode(request.getEmail(), request.getEmailCode());
        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @Operation(
            summary = "프로필 이메일 변경 시 인증코드 전송",
            description = "프로필 수정 시 새로운 이메일로 인증코드를 전송합니다."
    )
    @PostMapping("/update/send-code")
    public ResponseEntity<ApiResponse<?>> updateSendCode(@RequestBody EmailRequest request) {
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(request));
    }
}
