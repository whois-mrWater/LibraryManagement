package com.library.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * PERSON 3 - Reservation Management
 * 🎫 Model đại diện cho 1 lần đặt trước sách
 * 
 * Chức năng:
 * - Đặt trước sách (Make Reservation)
 * - Hủy đặt (Cancel Reservation)
 * - Xem danh sách đặt (View Reservations)
 * 
 * Status: ACTIVE, FULFILLED, CANCELLED
 */
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;        // Thành viên đặt

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Book book;            // Sách đặt

    @Column(nullable = false)
    private LocalDate reservationDate; // Ngày đặt

    @Column(nullable = false, length = 20)
    private String status;        // ACTIVE / FULFILLED / CANCELLED

    private LocalDate cancelledDate; // Ngày hủy (nếu cancelled)

    // ===== Getter & Setter =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getCancelledDate() { return cancelledDate; }
    public void setCancelledDate(LocalDate cancelledDate) { this.cancelledDate = cancelledDate; }
}
