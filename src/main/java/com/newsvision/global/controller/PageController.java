package com.newsvision.global.controller;

import com.newsvision.global.exception.CustomException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("title", "메인페이지");
        model.addAttribute("content", "news/main :: content");
        model.addAttribute("script", "/js/main.js");
        return "layout";
    }

    @GetMapping("/user/join")
    public String joinForm(Model model) {
        model.addAttribute("title", "회원가입");
        model.addAttribute("content", "user/join :: content");
        model.addAttribute("script", "/js/user/join.js");
        return "layout";
    }

    @GetMapping("/user/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "로그인");
        model.addAttribute("content", "user/login :: content");
        model.addAttribute("script", "/js/user/login.js");
        return "layout";
    }

    @GetMapping("/admin/manage")
    public String manage(Model model) {
        model.addAttribute("title", "관리자 페이지");
        model.addAttribute("content", "admin/manage :: content");
        return "layout";
    }

    @GetMapping("/news/article")
    public String article(Model model) {
        model.addAttribute("title", "아티클페이지");
        model.addAttribute("content", "news/article :: content");
        model.addAttribute("script", "/js/article.js");
        return "layout";
    }

    @GetMapping("/news/write")
    public String writeNewsPage(Model model) {
        model.addAttribute("title", "뉴스 작성");
        model.addAttribute("content", "news/write :: content");
        model.addAttribute("script", "/js/write.js");
        return "layout";
    }


    @GetMapping("/board/boards")
    public String boards(Model model) {
        model.addAttribute("title", "커뮤니티");
        model.addAttribute("content", "board/boards :: content");
        return "layout";
    }

    @GetMapping("/user/mypage")
    public String myPage(Model model) {
        model.addAttribute("title", "마이페이지");
        model.addAttribute("content", "user/mypage :: content");
        model.addAttribute("script", "/js/user/mypage.js");
        return "layout";
    }

    @GetMapping("/user/update")
    public String userUpdate(Model model) {
        model.addAttribute("title", "프로필편집");
        model.addAttribute("content", "user/update :: content");
        model.addAttribute("script", "/js/user/update.js");
        return "layout";
    }
}
