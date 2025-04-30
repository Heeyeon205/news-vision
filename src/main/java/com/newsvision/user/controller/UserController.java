package com.newsvision.user.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.global.jwt.JwtUtil;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.request.UpdatePasswordRequest;
import com.newsvision.user.dto.response.*;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "유저 API")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "ID 중복체크", description = "ID 중복체크")
    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<CheckUserUsernameResponse>> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserUsernameResponse(exists)));
    }

    @Operation(summary = "닉네임 중복체크", description = "닉네임 중복체크")
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<CheckUserNicknameResponse>> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.existsByNickname(nickname);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserNicknameResponse(exists)));
    }

    @Operation(summary = "비밀번호 확인", description = "비밀번호 확인")
    @GetMapping("/match-password")
    public ResponseEntity<ApiResponse<CheckUserPasswordResponse>> checkPassword(
            @RequestParam String password,
            @RequestParam String checkPassword
    ) {
        boolean exists = userService.checkPassword(password, checkPassword);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserPasswordResponse(exists)));
    }

    @Operation(summary = "유저 회원가입", description = "신규 유저 등록")
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> join(@RequestBody JoinUserRequest request) {
        userService.save(request);
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @Operation(summary = "유저 삭제", description = "유저 탈퇴")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Long>> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @Operation(summary = "유저 정보 수정 폼", description = "유저 정보 수정 폼")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateForm(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
       userService.matchUserId(userId, userDetails.getId());
        UpdateUserResponse response = userService.userInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "유저 정보 수정", description = "유저 정보 수정")
    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Long>> update(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "introduce", required = false) String introduce
    ) {
        userService.matchUserId(userId, userDetails.getId());
        userService.updateUserProfile(userId, image, nickname, email, introduce);
        return ResponseEntity.ok(ApiResponse.success(userId));
    }

    @Operation(summary = "비밀번호 수정폼 데이터", description = "비밀번호 수정폼 데이터")
    @GetMapping("/password-load")
    public ResponseEntity<ApiResponse<UpdateUsernameResponse>> loadData(
            HttpServletRequest request
    ) {
        String tempToken = JwtUtil.parsingToken(request.getHeader("Authorization"));
        String username = jwtTokenProvider.getUsername(tempToken);
        return ResponseEntity.ok(ApiResponse.success(new UpdateUsernameResponse(username)));
    }

    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
    @PostMapping("/new-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            HttpServletRequest request,
            @RequestBody UpdatePasswordRequest passwordRequestrequest
    ){
        String tempToken = JwtUtil.parsingToken(request.getHeader("Authorization"));
        userService.updatePassword(tempToken, passwordRequestrequest);
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @Operation(summary = "사용자 권한 확인", description = "사용자 권한 확인")
    @GetMapping("/check-role")
    public ResponseEntity<ApiResponse<String>> checkRole(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        userService.validateRole(customUserDetails.getRole());
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @Operation(summary = "로그인 사용자 확인", description = "로그인 사용자 확인")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null || userDetails.getId() == null){
            log.warn(" 인증 안 된 사용자 요청");
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        User user = userService.findByUserId(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(new UserInfoResponse(user)));
    }

}
