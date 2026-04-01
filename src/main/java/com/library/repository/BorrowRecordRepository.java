package com.library.repository;

import com.library.model.BorrowRecord;
import com.library.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * THANH VIEN C PHU TRACH
 */
@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    // TODO (Thanh vien C): Them cac ham tim kiem o day
    List<BorrowRecord> findByMember(Member member);
    List<BorrowRecord> findByStatus(String status);
}
