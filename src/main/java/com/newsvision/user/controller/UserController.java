package com.newsvision.user.controller;

import com.newsvision.global.response.ApiResponse;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.request.UpdateUserRequest;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinUserRequest>> join(@RequestBody JoinUserRequest request) {
        System.out.println("회원가입 요청: " + request);
        userService.existsByNickname(request.getNickname());
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
