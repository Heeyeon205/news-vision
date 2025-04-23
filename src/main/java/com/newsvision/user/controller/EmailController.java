package com.newsvision.user.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.user.dto.request.EmailRequest;
import com.newsvision.user.dto.request.VerifyEmailRequest;
import com.newsvision.user.dto.response.CheckUserUsernameResponse;
import com.newsvision.user.service.EmailService;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<?>> sendCode(@RequestBody EmailRequest request) {
        Boolean exists = userService.existsByEmail(request.getEmail());
        if (exists){
            return ResponseEntity.ok(ApiResponse.success(new CheckUserUsernameResponse(exists)));
        }
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyEmailRequest>> verifyCode(@RequestBody VerifyEmailRequest request) {
        log.info("verify email {}", request.getEmail());
        log.info("verify email code {}", request.getEmailCode());
        emailService.verifyCode(request.getEmail(), request.getEmailCode());
        return ResponseEntity.ok(ApiResponse.success(request));
    }
}
