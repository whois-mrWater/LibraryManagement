package com.library.controller;

import com.library.model.Member;
import com.library.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * THANH VIEN B PHU TRACH
 * URL goc: /members
 */
@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members/list";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("member", new Member());
        return "members/form";
    }

    @PostMapping("/register")
    public String registerMember(@ModelAttribute Member member) {
        // TODO (B): Goi memberService.registerMember(member)
        return "redirect:/members";
    }

    // TODO (B): Them cac endpoint edit, toggle-status
}
