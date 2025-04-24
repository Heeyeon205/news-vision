package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.admin.service.UserListService;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserListController {
    private final UserListService userListService;
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    @GetMapping
    public List<UserListResponse> getUserList() {
        return userListService.getAllUsers();
    }


    @GetMapping("/max")
    public ResponseEntity<List<UserListResponse>> getMaxAllUsers() {
        List<UserListResponse> users = userListService.getMaxAllUsers();
        return ResponseEntity.ok(users);
    }


    @PostMapping
    public UserListResponse createUser(@RequestBody UserListResponse userListResponse) {
        return userListService.saveUser(userListResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("사용자 삭제 실패 (ID: {}): {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 서버 오류 응답 (HTTP 500)
        }
    }
}