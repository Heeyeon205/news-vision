package com.newsvision.user.controller;

import com.newsvision.global.aws.S3Uploader;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.mypage.dto.response.UserBoardListResponse;
import com.newsvision.mypage.dto.response.UserNewsListResponse;
import com.newsvision.mypage.dto.response.UserNoticeListResponse;
import com.newsvision.mypage.dto.response.UserScrapListResponse;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.request.UpdatePasswordRequest;
import com.newsvision.user.dto.response.*;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthController authController;

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

    @GetMapping("/match-password")
    public ResponseEntity<ApiResponse<CheckUserPasswordResponse>> checkNickname(
            @RequestParam String password,
            @RequestParam String checkPassword
    ) {
        boolean exists = userService.checkPassword(password, checkPassword);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserPasswordResponse(exists)));
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinUserRequest>> join(@RequestBody JoinUserRequest request) {
        log.warn("request.getUsername() = {}", request.getUsername());
        log.warn("request.getPassword() = {}", request.getPassword());
        log.warn("request.getEmail() = {}", request.getEmail());
        userService.save(request);
        return ResponseEntity.ok(ApiResponse.success(request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Long>> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/info")
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
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "introduce", required = false) String introduce
    ) {
        Long id = userDetails.getId();
        userService.updateUserProfile(id, image, nickname, email, introduce);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/password-load")
    public ResponseEntity<ApiResponse<UpdateUsernameResponse>> loadData(
            HttpServletRequest request
    ) {
        String header = request.getHeader("Authorization");
        String tempToken = header.split(" ")[1];
        String username = jwtTokenProvider.getUsername(tempToken);
        UpdateUsernameResponse response = new UpdateUsernameResponse(username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/new-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            HttpServletRequest request,
            @RequestBody UpdatePasswordRequest passwordRequestrequest
    ){
        String header = request.getHeader("Authorization");
        String tempToken = header.split(" ")[1];
        String username = jwtTokenProvider.getUsername(tempToken);
        User user = userService.findByUsername(username);
        authController.deleteTempToken(tempToken);
        userService.updatePassword(user, passwordRequestrequest);
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }
}
