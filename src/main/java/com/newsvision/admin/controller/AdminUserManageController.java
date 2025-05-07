package com.newsvision.admin.controller;

import com.newsvision.admin.dto.response.UserListResponse;
import com.newsvision.admin.service.UserListService;
import com.newsvision.board.service.CommentService;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.poll.repository.PollVoteRepository;
import com.newsvision.poll.service.PollVoteService;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequiredArgsConstructor
@Where(clause = "is_deleted = false")
@RequestMapping("/admin/users")
public class AdminUserManageController {
    private final UserListService userListService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserListResponse>>> getUserList(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }
        List<UserListResponse> users = userListService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<UserListResponse>>> getMaxAllUsers(@AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }
        List<UserListResponse> users = userListService.getMaxAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(id));
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<ApiResponse<?>> updateRole(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User admin = userService.findByUserId(userDetails.getId());
        userService.checkAdminByUserId(admin.getRole().name());
        userService.updateUserRole(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}