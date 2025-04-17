package com.newsvision.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"", "/"})
    public String main(Model model) {
        model.addAttribute("title", "메인페이지");
        model.addAttribute("content", "news/main :: content");
        return "layout";
    }

    @GetMapping("/user/join")
    public String joinForm(Model model) {
        model.addAttribute("title", "회원가입");
        model.addAttribute("content", "user/join :: content");
        model.addAttribute("script", "/js/join.js");
        return "layout";
    }

    @GetMapping("/user/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "로그인");
        model.addAttribute("content", "user/login :: content");
        model.addAttribute("script", "/js/login.js");
        return "layout";
    }

    @GetMapping("/user/mypage")
    public String mypage(Model model) {
        model.addAttribute("title", "마이페이지");
        model.addAttribute("content", "user/mypage :: content");
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
        return "layout";
    }

    @GetMapping("/board/boards")
    public String boards(Model model) {
        model.addAttribute("title", "커뮤니티");
        model.addAttribute("content", "board/boards :: content");
        return "layout";
    }
}
