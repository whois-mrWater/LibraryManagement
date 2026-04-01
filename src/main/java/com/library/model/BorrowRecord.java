package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * PERSON 2 - Borrow/Return Management
 * 📚 Model đại diện cho 1 lượt mượn sách
 * 
 * Chức năng:
 * - Mượn sách (Borrow Book)
 * - Trả sách (Return Book)
 * - Kiểm tra tồn kho (Check Availability)
 * 
 * Status: BORROWED, RETURNED, OVERDUE
 */
@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;        // Thành viên mượn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Book book;            // Sách mượn

    @Column(nullable = false)
    private LocalDate borrowDate; // Ngày mượn
    
    @Column(nullable = false)
    private LocalDate dueDate;    // Ngày phải trả (borrowDate + 14 ngày)
    
    private LocalDate returnDate; // Ngày trả thực tế (null nếu chưa trả)
    
    @Column(nullable = false, length = 20)
    private String status;        // BORROWED / RETURNED / OVERDUE

    // ===== Getter & Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
