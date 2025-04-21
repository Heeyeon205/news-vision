package com.newsvision.user.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.user.dto.request.VerifyEmailRequest;
import com.newsvision.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<?>> sendCode(@RequestParam String email) {
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok(ApiResponse.success("인증 메일이 발송되었습니다."));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyCode(@RequestBody VerifyEmailRequest request) {
        emailService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ApiResponse.success("이메일 인증에 성공했습니다."));
    }
}
