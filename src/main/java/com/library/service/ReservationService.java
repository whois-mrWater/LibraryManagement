package com.library.service;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Reservation;
import com.library.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PERSON 3 - Reservation Service
 * 🎫 Business logic cho chức năng đặt sách
 * 
 * Chức năng:
 * - Tạo đặt sách (makeReservation)
 * - Hủy đặt (cancelReservation)
 * - Lấy danh sách đặt (getReservations)
 */
@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Đặt trước sách
     * @param member - Thành viên đặt
     * @param book - Sách cần đặt
     * @return Reservation object
     */
    public Reservation makeReservation(Member member, Book book) {
        if (member == null || book == null) {
            throw new IllegalArgumentException("Member and Book cannot be null");
        }

        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("ACTIVE");

        return reservationRepository.save(reservation);
    }

    /**
     * Hủy đặt sách
     * @param reservationId - ID của đặt sách cần hủy
     * @return true nếu hủy thành công
     */
    public boolean cancelReservation(Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if (reservation.isPresent()) {
            Reservation res = reservation.get();
            res.setStatus("CANCELLED");
            res.setCancelledDate(LocalDate.now());
            reservationRepository.save(res);
            return true;
        }
        return false;
    }

    /**
     * Lấy tất cả đặt sách của một thành viên
     * @param member - Thành viên
     * @return Danh sách đặt sách
     */
    public List<Reservation> getReservationsByMember(Member member) {
        return reservationRepository.findByMember(member);
    }

    /**
     * Lấy tất cả đặt sách còn hoạt động
     * @return Danh sách đặt sách ACTIVE
     */
    public List<Reservation> getActiveReservations() {
        return reservationRepository.findByStatus("ACTIVE");
    }

    /**
     * Lấy đặt sách theo ID
     * @param id - ID của đặt sách
     * @return Reservation object hoặc null
     */
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    /**
     * Lấy tất cả đặt sách
     * @return Danh sách tất cả đặt sách
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Xóa đặt sách
     * @param id - ID của đặt sách
     */
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
