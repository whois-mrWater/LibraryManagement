package com.library.model;

import jakarta.persistence.*;

/**
 * PERSON 1 - Book Management
 * 📖 Model đại diện cho 1 quyển sách trong thư viện
 * 
 * Chức năng:
 * - Thêm sách (Add Book)
 * - Sửa sách (Edit Book)
 * - Xóa sách (Delete Book)
 */
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String bookCode;       // Mã sách (BK001, BK002, ...)
    
    @Column(nullable = false, length = 255)
    private String title;          // Tên sách
    
    @Column(nullable = false, length = 255)
    private String author;         // Tác giả
    
    @Column(nullable = false, length = 255)
    private String publisher;      // Nhà xuất bản (Kim Đồng, Trẻ, ...)
    
    @Column(length = 100)
    private String category;       // Thể loại (Văn học, KH-KT, ...)
    
    private int totalCopies;       // Tổng số bản
    private int availableCopies;   // Số bản còn trống

    // ===== Getter & Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBookCode() { return bookCode; }
    public void setBookCode(String bookCode) { this.bookCode = bookCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
}
