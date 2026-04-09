package com.library.controller;

import com.library.model.Member;
import com.library.repository.MemberRepository;
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
        memberService.registerMember(member);
        return "redirect:/members";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Long id) {
        memberService.toggleMemberStatus(id);
        return "redirect:/members";
    }

    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id) {
        System.out.println(">>> DELETE called for id: " + id); // thêm dòng này
        memberService.deleteMember(id);
        return "redirect:/members";
    }

    // Hiện form sửa
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.findById(id)); // ✅
        return "members/edit";
    }

    // Xử lý lưu
    @PostMapping("/{id}/edit")
    public String editSave(@PathVariable Long id,
            @ModelAttribute Member updated) {
        memberService.updateMember(id, updated); // ✅
        return "redirect:/members";
    }

}
