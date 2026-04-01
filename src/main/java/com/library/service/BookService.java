package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * PERSON 1 - Book Service
 * 📚 Quản lý Sách: them, sua, xoa, tim kiem
 * 
 * Methods:
 * - getAllBooks() - Lấy danh sách tất cả sách
 * - getBookById(id) - Lấy chi tiết sách
 * - addBook(book) - Thêm sách mới
 * - updateBook(id, book) - Cập nhật sách
 * - deleteBook(id) - Xoá sách
 * - searchByTitle(title) - Tìm sách theo tiêu đề
 * - searchByAuthor(author) - Tìm sách theo tác giả
 * - searchBooks(keyword) - Tìm sách theo từ khóa
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    /** Lấy danh sách tất cả sách */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /** Lấy chi tiết sách theo ID */
    public Book getBookById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    /** Thêm sách mới - kiểm tra mã sách trùng không */
    public Book addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Thông tin sách không được null");
        }
        if (book.getBookCode() == null || book.getBookCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sách không được trống");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Tiêu đề sách không được trống");
        }
        return bookRepository.save(book);
    }

    /** Cập nhật thông tin sách */
    public Book updateBook(Long id, Book updatedBook) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            if (updatedBook.getTitle() != null) {
                book.setTitle(updatedBook.getTitle());
            }
            if (updatedBook.getAuthor() != null) {
                book.setAuthor(updatedBook.getAuthor());
            }
            if (updatedBook.getPublisher() != null) {
                book.setPublisher(updatedBook.getPublisher());
            }
            if (updatedBook.getCategory() != null) {
                book.setCategory(updatedBook.getCategory());
            }
            if (updatedBook.getTotalCopies() > 0) {
                book.setTotalCopies(updatedBook.getTotalCopies());
            }
            if (updatedBook.getAvailableCopies() >= 0) {
                book.setAvailableCopies(updatedBook.getAvailableCopies());
            }
            return bookRepository.save(book);
        }
        return null;
    }

    /** Xoá sách theo ID */
    public void deleteBook(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        bookRepository.deleteById(id);
    }

    /** Tìm sách theo tiêu đề */
    public List<Book> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /** Tìm sách theo tác giả */
    public List<Book> searchByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    /** Tìm sách theo từ khóa (tiêu đề hoặc tác giả) */
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }
}
