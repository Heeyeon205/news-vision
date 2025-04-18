package com.newsvision.user.controller;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.response.ApiResponse;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.request.UpdateUserRequest;
import com.newsvision.user.dto.response.CheckUserNicknameResponse;
import com.newsvision.user.dto.response.CheckUserUsernameResponse;
import com.newsvision.user.service.EmailService;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<CheckUserUsernameResponse>> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserUsernameResponse(exists)));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<CheckUserNicknameResponse>> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.existsByNickname(nickname);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserNicknameResponse(exists)));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinUserRequest>> join(@RequestBody JoinUserRequest request) {
        userService.save(request);
        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Long>> delete(@PathVariable Long id) {
        userService.findByUserId(id);
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<UpdateUserRequest>> update(@RequestBody UpdateUserRequest request) {
        userService.existsByNickname(request.getNickname());
        userService.update(request);
        return ResponseEntity.ok(ApiResponse.success(request));
    }
}
