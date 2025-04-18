package com.newsvision.user.controller;

import com.newsvision.global.aws.S3Uploader;
import com.newsvision.global.response.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.response.CheckUserNicknameResponse;
import com.newsvision.user.dto.response.CheckUserUsernameResponse;
import com.newsvision.user.dto.response.UpdateUserResponse;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final S3Uploader s3Uploader;

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

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Long>> delete(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long id = customUserDetails.getId();
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/update")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        UpdateUserResponse response = userService.userInfo(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "introduce", required = false) String introduce
    ) {
        Long id = userDetails.getId();
        userService.updateUserProfile(id, image, nickname, introduce);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
