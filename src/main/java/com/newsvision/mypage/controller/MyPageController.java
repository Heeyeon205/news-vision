package com.newsvision.mypage.controller;

import com.newsvision.user.entity.Follow;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final UserService userService;
}
