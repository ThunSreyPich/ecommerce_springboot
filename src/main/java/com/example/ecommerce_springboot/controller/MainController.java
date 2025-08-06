package com.example.ecommerce_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String shop() {
        return "shop"; // shop.html
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // dashboard.html
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login"; // login.html
//    }
}