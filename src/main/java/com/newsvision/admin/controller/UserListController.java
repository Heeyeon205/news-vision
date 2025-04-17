package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.admin.service.UserListService;
import com.newsvision.user.repository.UserRepository;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserListController {
    private final UserListService userService;

    @GetMapping("/users")
    public List<UserListResponse> getUserList() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public UserListResponse createUser(@RequestBody UserListResponse userListResponse) {
        return userService.saveUser(userListResponse);
    }
}
