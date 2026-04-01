package com.library.repository;

import com.library.model.Member;
import com.library.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PERSON 3 - Reservation Repository
 * 🎫 Truy cập dữ liệu đặt sách từ database
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    /**
     * Tìm tất cả đặt sách của một thành viên
     */
    List<Reservation> findByMember(Member member);

    /**
     * Tìm tất cả đặt sách còn hoạt động (ACTIVE)
     */
    List<Reservation> findByStatus(String status);

    /**
     * Tìm đặt sách của thành viên theo trạng thái
     */
    List<Reservation> findByMemberAndStatus(Member member, String status);
}
