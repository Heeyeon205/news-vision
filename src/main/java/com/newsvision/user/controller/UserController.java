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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "유저 컨트롤러", description = "유저 API")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "ID 중복체크", description = "회원가입 시 아이디(Username)가 이미 사용 중인지 확인합니다.")
    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<CheckUserUsernameResponse>> checkUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserUsernameResponse(exists)));
    }

    @Operation(summary = "닉네임 중복체크", description = "닉네임이 이미 사용 중인지 확인합니다.")
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<CheckUserNicknameResponse>> checkNickname(@RequestParam String nickname) {
        boolean exists = userService.existsByNickname(nickname);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserNicknameResponse(exists)));
    }

    @Operation(summary = "비밀번호 확인", description = "입력한 두 비밀번호가 일치하는지 확인합니다.")
    @GetMapping("/match-password")
    public ResponseEntity<ApiResponse<CheckUserPasswordResponse>> checkPassword(
            @RequestParam String password,
            @RequestParam String checkPassword
    ) {
        boolean exists = userService.checkPassword(password, checkPassword);
        return ResponseEntity.ok(ApiResponse.success(new CheckUserPasswordResponse(exists)));
    }

    @Operation(summary = "유저 회원가입", description = "신규 사용자를 등록합니다.")
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> join(@RequestBody JoinUserRequest request) {
        userService.save(request);
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @Operation(summary = "유저 탈퇴", description = "로그인한 사용자의 계정을 삭제합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Long>> delete(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = userDetails.getId();
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @Operation(summary = "유저 정보 수정 폼", description = "특정 사용자의 정보를 반환합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UpdateUserResponse>> updateForm(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.matchUserId(userId, userDetails.getId());
        UpdateUserResponse response = userService.userInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "유저 정보 수정", description = "닉네임, 이메일, 자기소개, 프로필 이미지를 수정합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UpdateResultResponse>> update(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "introduce", required = false) String introduce
    ) {
        userService.matchUserId(userId, userDetails.getId());
        userService.updateUserProfile(userId, image, nickname, email, introduce);

        return ResponseEntity.ok(ApiResponse.success(new UpdateResultResponse(userService.findByUserId(userId))));
    }

    @Operation(summary = "비밀번호 수정폼 데이터", description = "임시 토큰을 기반으로 사용자 ID(아이디)를 반환합니다.")
    @GetMapping("/password-load")
    public ResponseEntity<ApiResponse<UpdateUsernameResponse>> loadData(HttpServletRequest request) {
        String tempToken = JwtUtil.parsingToken(request.getHeader("Authorization"));
        String username = jwtTokenProvider.getUsername(tempToken);
        return ResponseEntity.ok(ApiResponse.success(new UpdateUsernameResponse(username)));
    }

    @Operation(summary = "비밀번호 수정", description = "임시 토큰과 새 비밀번호를 이용해 비밀번호를 변경합니다.")
    @PostMapping("/new-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            HttpServletRequest request,
            @RequestBody UpdatePasswordRequest passwordRequestrequest
    ){
        String tempToken = JwtUtil.parsingToken(request.getHeader("Authorization"));
        userService.updatePassword(tempToken, passwordRequestrequest);
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @Operation(summary = "사용자 권한 확인", description = "현재 로그인한 사용자의 권한이 ADMIN, CREATOR, USER 중 어떤 것인지 확인합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/check-role")
    public ResponseEntity<ApiResponse<String>> checkRole(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.validateRole(customUserDetails.getRole());
        return ResponseEntity.ok(ApiResponse.success("ok"));
    }

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null || userDetails.getId() == null){
            log.warn(" 인증 안 된 사용자 요청");
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        User user = userService.findByUserId(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(new UserInfoResponse(user)));
    }
}
