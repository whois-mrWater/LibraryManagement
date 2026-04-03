package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.model.Book;
import com.library.service.BookService;

/**
 * THANH VIEN A PHU TRACH
 * Xu ly cac request HTTP lien quan den Sach
 * URL goc: /books
 */
@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /** GET /books -> Trang tìm kiếm sách */
    @GetMapping
    public String listBooks(@RequestParam(required = false) String keyword, Model model) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("books", bookService.searchBooks(keyword));
            model.addAttribute("keyword", keyword); // giữ chữ trong ô tìm kiếm
        } else {
            model.addAttribute("books", bookService.getAllBooks());
        }
        return "books/list";
    }


    /** GET /books/new -> trang them sach */
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/form";
    }

    /** POST /books/new -> xu ly them sach */
    @PostMapping("/new")
    public String addBook(@ModelAttribute Book book, Model model) {
        try {
            bookService.addBook(book);           // Gọi Service lưu vào DB
            return "redirect:/books";            // Lưu xong → về danh sách
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage()); // Có lỗi → hiện thông báo
            return "books/form";                 // Quay lại form
        }
    }


    /** GET /books/edit/{id} -> trang sua sach */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);  // Lấy sách từ DB theo ID
        model.addAttribute("book", book);         // Nhét vào model → form tự điền sẵn
        return "books/form";                      // Dùng chung form với thêm mới
    }


    /** POST /books/edit/{id} -> xu ly sua sach */
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book, Model model) {
        try {
            bookService.updateBook(id, book);    // Gọi Service cập nhật DB
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "books/form";
        }
    }

    /** POST /books/delete/{id} -> xoa sach */
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);             // Gọi Service xóa khỏi DB
        return "redirect:/books";
    }
    
}
