package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.admin.service.UserListService;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/users")
public class UserListController {
    private final UserRepository userRepository;
    private final UserListService userService;




    @GetMapping("/users")
    public String getUserList(Model model) {
        List<UserListResponse> users = userService.getAllUsers();
        model.addAttribute("사용자", users);
        return "userList";
    }
}
