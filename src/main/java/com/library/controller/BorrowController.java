package com.library.controller;

import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * THANH VIEN C PHU TRACH
 * URL goc: /borrow
 */
@Controller
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @GetMapping
    public String listBorrows(Model model) {
        model.addAttribute("records", borrowService.getOverdueBooks());
        return "borrow/list";
    }

    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long memberId, @RequestParam Long bookId) {
        // TODO (C): Goi borrowService.borrowBook(memberId, bookId)
        return "redirect:/borrow";
    }

    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        // TODO (C): Goi borrowService.returnBook(id)
        return "redirect:/borrow";
    }
}
