package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * PERSON 2 - Borrow/Return Management (Fine Calculation)
 * 💰 Model đại diện cho 1 lần phạt quá hạn
 * 
 * Tính phạt: overdueDays × 10,000 VND/ngày
 * Liên kết: BorrowRecord (1:1)
 */
@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, unique = true)
    private BorrowRecord borrowRecord; // Liên kết với lượt mượn

    @Column(nullable = false)
    private int overdueDays;      // Số ngày trễ

    @Column(nullable = false)
    private double amount;        // Số tiền phạt (overdueDays × 10,000)

    @Column(nullable = false)
    private boolean paid;         // Đã đóng phạt chưa?

    @Column(nullable = false)
    private LocalDate fineDate;   // Ngày tính phạt

    // ===== Getter & Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BorrowRecord getBorrowRecord() { return borrowRecord; }
    public void setBorrowRecord(BorrowRecord borrowRecord) { this.borrowRecord = borrowRecord; }

    public int getOverdueDays() { return overdueDays; }
    public void setOverdueDays(int overdueDays) { this.overdueDays = overdueDays; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public LocalDate getFineDate() { return fineDate; }
    public void setFineDate(LocalDate fineDate) { this.fineDate = fineDate; }
}
