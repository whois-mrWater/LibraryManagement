package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    /** GET /books -> trang danh sach sach */
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/list"; // -> src/main/resources/templates/books/list.html
    }

    /** GET /books/add -> trang them sach */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/form";
    }

    /** POST /books/add -> xu ly them sach */
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        // TODO (A): Goi bookService.addBook(book)
        return "redirect:/books";
    }

    /** GET /books/edit/{id} -> trang sua sach */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        // TODO (A): Implement
        return "books/form";
    }

    /** POST /books/edit/{id} -> xu ly sua sach */
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        // TODO (A): Implement
        return "redirect:/books";
    }

    /** POST /books/delete/{id} -> xoa sach */
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        // TODO (A): Implement
        return "redirect:/books";
    }
}
