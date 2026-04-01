package com.library.service;

import com.library.model.Fine;
import com.library.model.BorrowRecord;
import com.library.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * PERSON D - Fine Management Service
 * Chức năng: Tính tiền phạt, đánh dấu đóng phạt, thống kê
 */
@Service
public class FineService {

    @Autowired
    private FineRepository fineRepository;

    public static final double FINE_PER_DAY = 2000.0; // 2000 VND/ngày trễ

    /**
     * Tính tiền phạt: overdueDays * 2000 VND
     * Tối thiểu 0, không có tiền phạt âm
     */
    public double calculateFine(int overdueDays) {
        if (overdueDays <= 0) {
            return 0.0;
        }
        return overdueDays * FINE_PER_DAY;
    }

    /**
     * Tạo mới bản ghi phạt từ lượt mượn quá hạn
     */
    public Fine createFine(BorrowRecord borrowRecord) {
        if (borrowRecord == null || borrowRecord.getReturnDate() == null) {
            return null;
        }

        // Tính số ngày trễ
        LocalDate dueDate = borrowRecord.getDueDate();
        LocalDate returnDate = borrowRecord.getReturnDate();
        
        if (returnDate.isBefore(dueDate) || returnDate.isEqual(dueDate)) {
            return null; // Không trễ, không phạt
        }

        long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnDate);
        double fineAmount = calculateFine((int) overdueDays);

        Fine fine = new Fine();
        fine.setBorrowRecord(borrowRecord);
        fine.setOverdueDays((int) overdueDays);
        fine.setAmount(fineAmount);
        fine.setPaid(false);
        fine.setFineDate(LocalDate.now());

        return fineRepository.save(fine);
    }

    /**
     * Đánh dấu đã đóng phạt
     */
    public Fine payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
            .orElseThrow(() -> new RuntimeException("Fine not found with id: " + fineId));
        fine.setPaid(true);
        return fineRepository.save(fine);
    }

    /**
     * Lấy danh sách phạt chưa đóng
     */
    public List<Fine> getUnpaidFines() {
        return fineRepository.findByPaid(false);
    }

    /**
     * Lấy danh sách tất cả phạt đã đóng
     */
    public List<Fine> getPaidFines() {
        return fineRepository.findByPaid(true);
    }

    /**
     * Lấy chi tiết 1 phạt
     */
    public Fine getFineById(Long fineId) {
        return fineRepository.findById(fineId)
            .orElseThrow(() -> new RuntimeException("Fine not found with id: " + fineId));
    }

    /**
     * Lấy tất cả phạt
     */
    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }

    /**
     * Thống kê tổng tiền phạt theo tháng
     */
    public double getTotalFinesByMonth(int year, int month) {
        List<Fine> allFines = fineRepository.findAll();
        return allFines.stream()
            .filter(fine -> {
                LocalDate fineDate = fine.getFineDate();
                return fineDate.getYear() == year && fineDate.getMonthValue() == month;
            })
            .mapToDouble(Fine::getAmount)
            .sum();
    }

    /**
     * Tổng tiền phạt chưa đóng
     */
    public double getTotalUnpaidAmount() {
        return fineRepository.findByPaid(false).stream()
            .mapToDouble(Fine::getAmount)
            .sum();
    }
}
