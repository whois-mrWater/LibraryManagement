package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required=false) String error, Model model) {
        if (error != null) {
            model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu!");
        }
        return "login"; // → trả về login.html
    }   
}
