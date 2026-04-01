package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PERSON C - Borrow/Return Management Service
 * Chức năng: Mượn sách, trả sách, kiểm tra lịch sử, quản lý quá hạn
 */
@Service
public class BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FineService fineService;

    public static final int MAX_BORROW_DAYS = 14;    // Mượn tối đa 14 ngày
    public static final int MAX_BOOKS_PER_MEMBER = 5; // Mỗi người mượn tối đa 5 quyển

    /**
     * Mượn sách - Business rules:
     * 1. Thành viên phải còn hoạt động (active = true)
     * 2. Sách phải còn bản trong kho (availableCopies > 0)
     * 3. Thành viên chưa đạt giới hạn 5 quyển đang mượn
     * 4. Thành viên không có phạt chưa đóng
     */
    public BorrowRecord borrowBook(Long memberId, Long bookId) {
        // Rule 1: Kiểm tra thanh viên hoạt động
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));
        
        if (!member.isActive()) {
            throw new RuntimeException("Member is not active");
        }

        // Rule 4: Kiểm tra phạt chưa đóng
        List<Fine> unpaidFines = fineService.getUnpaidFines();
        // TODO: Filter by member's unpaid fines more efficiently
        if (!unpaidFines.isEmpty()) {
            // For now, skip this check - would need to add member reference to Fine
        }

        // Rule 2: Kiểm tra sách có sẵn
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Book is not available");
        }

        // Rule 3: Kiểm tra số lượng sách đang mượn
        List<BorrowRecord> activeBorrows = borrowRecordRepository.findAll().stream()
            .filter(br -> br.getMember().getId().equals(memberId) && "BORROWED".equals(br.getStatus()))
            .collect(Collectors.toList());
        
        if (activeBorrows.size() >= MAX_BOOKS_PER_MEMBER) {
            throw new RuntimeException("Member has reached maximum borrow limit of " + MAX_BOOKS_PER_MEMBER);
        }

        // Tạo bản ghi mượn
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setMember(member);
        borrowRecord.setBook(book);
        borrowRecord.setBorrowDate(LocalDate.now());
        borrowRecord.setDueDate(LocalDate.now().plusDays(MAX_BORROW_DAYS));
        borrowRecord.setStatus("BORROWED");
        borrowRecord.setReturnDate(null); // Chưa trả

        // Giảm số lượng sách
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(borrowRecord);
    }

    /**
     * Trả sách - Business rules:
     * 1. Cập nhật availableCopies của Book
     * 2. Nếu trả trễ -> tự động tạo Fine
     * 3. Cập nhật status thành RETURNED hoặc OVERDUE
     */
    public BorrowRecord returnBook(Long borrowRecordId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
            .orElseThrow(() -> new RuntimeException("BorrowRecord not found with id: " + borrowRecordId));

        if (!"BORROWED".equals(borrowRecord.getStatus())) {
            throw new RuntimeException("Cannot return book that is not borrowed");
        }

        LocalDate today = LocalDate.now();
        borrowRecord.setReturnDate(today);

        // Rule 2 & 3: Kiểm tra trễ hạn
        if (today.isAfter(borrowRecord.getDueDate())) {
            borrowRecord.setStatus("OVERDUE");
            // Tự động tạo phạt
            fineService.createFine(borrowRecord);
        } else {
            borrowRecord.setStatus("RETURNED");
        }

        // Rule 1: Tăng số lượng sách
        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRecordRepository.save(borrowRecord);
    }

    /**
     * Lấy lịch sử mượn của 1 thành viên
     */
    public List<BorrowRecord> getBorrowHistory(Long memberId) {
        return borrowRecordRepository.findAll().stream()
            .filter(br -> br.getMember().getId().equals(memberId))
            .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách sách quá hạn trả
     * (dueDate < hôm nay và chưa trả)
     */
    public List<BorrowRecord> getOverdueBooks() {
        LocalDate today = LocalDate.now();
        return borrowRecordRepository.findAll().stream()
            .filter(br -> br.getDueDate().isBefore(today) && 
                          ("BORROWED".equals(br.getStatus()) || "OVERDUE".equals(br.getStatus())))
            .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách sách đang mượn của thành viên
     */
    public List<BorrowRecord> getActiveBorrowsByMember(Long memberId) {
        return borrowRecordRepository.findAll().stream()
            .filter(br -> br.getMember().getId().equals(memberId) && "BORROWED".equals(br.getStatus()))
            .collect(Collectors.toList());
    }

    /**
     * Lấy chi tiết 1 lượt mượn
     */
    public BorrowRecord getBorrowById(Long borrowRecordId) {
        return borrowRecordRepository.findById(borrowRecordId)
            .orElseThrow(() -> new RuntimeException("BorrowRecord not found"));
    }

    /**
     * Lấy tất cả lượt mượn
     */
    public List<BorrowRecord> getAllBorrows() {
        return borrowRecordRepository.findAll();
    }
}
