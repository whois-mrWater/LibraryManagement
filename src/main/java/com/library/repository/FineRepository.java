package com.library.repository;

import com.library.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * THANH VIEN D PHU TRACH
 */
@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    // TODO (Thanh vien D): Them cac ham tim kiem o day
    List<Fine> findByPaid(boolean paid);
}
