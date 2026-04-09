package com.library.controller;

import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List; // ← đúng import

@Controller
public class HomeController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            // Tính lại available_copies tự động
            List<Book> books = bookRepository.findAll();
            for (Book book : books) {
                long borrowed = borrowRecordRepository.countByBookAndStatusIn(
                        book, List.of("BORROWED", "OVERDUE"));
                book.setAvailableCopies((int) (book.getTotalCopies() - borrowed));
            }
            bookRepository.saveAll(books);
            model.addAttribute("books", books);

            // Load sách đã mượn cho Member
            memberRepository.findByPhone(principal.getName()).ifPresent(member -> model.addAttribute("borrowRecords",
                    borrowRecordRepository.findByMember(member)));
        }
        return "index";
    }
}
