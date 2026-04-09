package com.library.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.model.Member;
import com.library.model.User;
import com.library.repository.MemberRepository;
import com.library.repository.UserRepository;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestParam String phone,
            @RequestParam String email,
            @RequestParam String fullName, // ← thêm
            Model model) {

        if (userRepository.existsByUsername(phone)) {
            model.addAttribute("registerError", "Số điện thoại này đã được đăng ký!");
            return "login";
        }

        Member member = new Member();
        member.setPhone(phone);
        member.setEmail(email);
        member.setFullName(fullName); // ← thay "Thành viên mới"
        member.setJoinDate(LocalDate.now());
        member.setActive(true);
        member.setMemberCode("M" + System.currentTimeMillis() % 10000);
        memberRepository.save(member);

        User user = new User();
        user.setUsername(phone);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole("MEMBER");
        user.setEmail(email);
        user.setEnabled(true);
        userRepository.save(user);

        model.addAttribute("registerSuccess",
                "✅ Đăng ký thành công! Mật khẩu mặc định là 123456");
        return "login";
    }

}
