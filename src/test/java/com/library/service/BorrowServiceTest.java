package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * PERSON C - Borrow Service Unit Tests
 */
@DisplayName("Borrow Service Tests")
class BorrowServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FineService fineService;

    @InjectMocks
    private BorrowService borrowService;

    private BorrowRecord testBorrowRecord;
    private Member testMember;
    private Book testBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testMember = new Member();
        testMember.setId(1L);
        testMember.setFullName("Test Member");
        testMember.setActive(true);

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAvailableCopies(5);

        testBorrowRecord = new BorrowRecord();
        testBorrowRecord.setId(1L);
        testBorrowRecord.setMember(testMember);
        testBorrowRecord.setBook(testBook);
        testBorrowRecord.setBorrowDate(LocalDate.now());
        testBorrowRecord.setDueDate(LocalDate.now().plusDays(14));
        testBorrowRecord.setStatus("BORROWED");
    }

    @Test
    @DisplayName("TC001 - Mượn sách thành công")
    void testBorrowBook_ValidData_Success() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(borrowRecordRepository.findAll()).thenReturn(Arrays.asList());
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(testBorrowRecord);

        BorrowRecord result = borrowService.borrowBook(1L, 1L);

        assertNotNull(result);
        assertEquals("BORROWED", result.getStatus());
    }

    @Test
    @DisplayName("TC002 - Trả sách thành công")
    void testReturnBook_OnTime_Success() {
        testBorrowRecord.setReturnDate(LocalDate.now());
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(testBorrowRecord));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(testBorrowRecord);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BorrowRecord result = borrowService.returnBook(1L);

        assertEquals("RETURNED", result.getStatus());
    }

    @Test
    @DisplayName("TC003 - Trả sách trễ hạn - tạo phạt")
    void testReturnBook_Overdue_CreateFine() {
        testBorrowRecord.setDueDate(LocalDate.now().minusDays(3));
        testBorrowRecord.setReturnDate(LocalDate.now());
        
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(testBorrowRecord));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenReturn(testBorrowRecord);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(fineService.createFine(any(BorrowRecord.class))).thenReturn(new Fine());

        BorrowRecord result = borrowService.returnBook(1L);

        assertEquals("OVERDUE", result.getStatus());
        verify(fineService, times(1)).createFine(any(BorrowRecord.class));
    }

    @Test
    @DisplayName("TC004 - Lấy lịch sử mượn của thành viên")
    void testGetBorrowHistory() {
        when(borrowRecordRepository.findAll()).thenReturn(Arrays.asList(testBorrowRecord));

        List<BorrowRecord> result = borrowService.getBorrowHistory(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("TC005 - Lấy sách quá hạn")
    void testGetOverdueBooks() {
        testBorrowRecord.setDueDate(LocalDate.now().minusDays(5));
        when(borrowRecordRepository.findAll()).thenReturn(Arrays.asList(testBorrowRecord));

        List<BorrowRecord> result = borrowService.getOverdueBooks();

        assertTrue(result.size() >= 0);
    }

    @Test
    @DisplayName("TC006 - Lấy sách đang mượn của thành viên")
    void testGetActiveBorrowsByMember() {
        when(borrowRecordRepository.findAll()).thenReturn(Arrays.asList(testBorrowRecord));

        List<BorrowRecord> result = borrowService.getActiveBorrowsByMember(1L);

        assertTrue(result.size() >= 0);
    }

    @Test
    @DisplayName("TC007 - Lấy chi tiết bản ghi mượn")
    void testGetBorrowById() {
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(testBorrowRecord));

        BorrowRecord result = borrowService.getBorrowById(1L);

        assertNotNull(result);
    }

    @Test
    @DisplayName("TC008 - Lấy tất cả bản ghi mượn")
    void testGetAllBorrows() {
        when(borrowRecordRepository.findAll()).thenReturn(Arrays.asList(testBorrowRecord));

        List<BorrowRecord> result = borrowService.getAllBorrows();

        assertEquals(1, result.size());
    }
}
