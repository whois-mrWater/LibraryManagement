package com.library.controller;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Reservation;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * PERSON 3 - Reservation Controller
 * 🎫 UI endpoints cho chức năng đặt sách
 * 
 * Routes:
 * - GET /reservations - Xem danh sách đặt
 * - GET /reservations/add - Form đặt sách
 * - POST /reservations/add - Xử lý đặt sách
 * - GET /reservations/cancel/{id} - Hủy đặt
 */
@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Xem danh sách tất cả đặt sách
     */
    @GetMapping
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations/list";
    }

    /**
     * Hiển thị form để đặt sách
     */
    @GetMapping("/add")
    public String showReservationForm(Model model) {
        List<Member> members = memberRepository.findAll();
        List<Book> books = bookRepository.findAll();
        model.addAttribute("members", members);
        model.addAttribute("books", books);
        return "reservations/form";
    }

    /**
     * Xử lý thêm đặt sách mới
     */
    @PostMapping("/add")
    public String addReservation(
            @RequestParam Long memberId,
            @RequestParam Long bookId,
            Model model) {
        try {
            Optional<Member> member = memberRepository.findById(memberId);
            Optional<Book> book = bookRepository.findById(bookId);

            if (member.isPresent() && book.isPresent()) {
                Reservation reservation = reservationService.makeReservation(member.get(), book.get());
                model.addAttribute("message", "Đặt sách thành công!");
                return "redirect:/reservations";
            } else {
                model.addAttribute("error", "Thành viên hoặc sách không tồn tại!");
                return "reservations/form";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "reservations/form";
        }
    }

    /**
     * Hủy đặt sách
     */
    @GetMapping("/cancel/{id}")
    public String cancelReservation(
            @PathVariable Long id,
            Model model) {
        if (reservationService.cancelReservation(id)) {
            model.addAttribute("message", "Hủy đặt sách thành công!");
        } else {
            model.addAttribute("error", "Không tìm thấy đặt sách!");
        }
        return "redirect:/reservations";
    }

    /**
     * Xem chi tiết đặt sách
     */
    @GetMapping("/{id}")
    public String viewReservation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation != null) {
            model.addAttribute("reservation", reservation);
            return "reservations/detail";
        }
        return "redirect:/reservations";
    }
}
