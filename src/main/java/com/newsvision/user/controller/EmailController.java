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
@RequestMapping("/email")
@Tag(name = "EmailController", description = "이메일 인증 API")
public class EmailController {
    private final EmailService emailService;
    private final UserService userService;

    @Operation(summary = "인증코드 전송", description = "인증코드 전송")
    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<?>> sendCode(@RequestBody EmailRequest request) {
        Boolean exists = userService.existsByEmail(request.getEmail());
        if (exists){
            return ResponseEntity.ok(ApiResponse.success(new CheckUserUsernameResponse(exists)));
        }
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @Operation(summary = "인증코드 식별", description = "인증코드 식별")
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyEmailRequest>> verifyCode(@RequestBody VerifyEmailRequest request) {
        emailService.verifyCode(request.getEmail(), request.getEmailCode());
        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @Operation(summary = "프로필 업데이트시 인증코드 전송", description = "프로필 업데이트시 인증코드 전송")
    @PostMapping("/update/send-code")
    public ResponseEntity<ApiResponse<?>> updateSendCode(@RequestBody EmailRequest request) {
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(request));
    }
}
