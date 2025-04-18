package com.newsvision.mypage.controller;

import com.newsvision.global.response.ApiResponse;
import com.newsvision.mypage.dto.response.PortionUserResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/user")
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<PortionUserResponse>> userInfo(@PathVariable Long id) {
        User user = userService.findByUserId(id);
        PortionUserResponse response = new PortionUserResponse(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
