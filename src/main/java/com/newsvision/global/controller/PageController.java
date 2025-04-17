package com.newsvision.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"", "/"})
    public String main (){
        return "main";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
