package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.admin.service.UserListService;
import com.newsvision.global.exception.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<UserListResponse>>> getUserList() {
        List<UserListResponse> users = userListService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<UserListResponse>>> getMaxAllUsers() {
        List<UserListResponse> users = userListService.getMaxAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }


    @PostMapping
    public UserListResponse createUser(@RequestBody UserListResponse userListResponse) {
        return userListService.saveUser(userListResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {

            userService.delete(id);
           return ResponseEntity.ok(ApiResponse.success());

    }
}