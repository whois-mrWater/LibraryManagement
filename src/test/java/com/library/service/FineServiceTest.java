package com.library.service;

import com.library.model.Fine;
import com.library.model.BorrowRecord;
import com.library.repository.FineRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * PERSON D - Fine Service Unit Tests
 */
@DisplayName("Fine Service Tests")
class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @InjectMocks
    private FineService fineService;

    private Fine testFine;
    private BorrowRecord testBorrowRecord;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testBorrowRecord = new BorrowRecord();
        testBorrowRecord.setId(1L);
        testBorrowRecord.setDueDate(LocalDate.now().minusDays(3));
        testBorrowRecord.setReturnDate(LocalDate.now());

        testFine = new Fine();
        testFine.setId(1L);
        testFine.setOverdueDays(3);
        testFine.setAmount(6000.0);
        testFine.setPaid(false);
        testFine.setFineDate(LocalDate.now());
    }

    @Test
    @DisplayName("TC001 - Tính phí 0 ngày - trả về 0")
    void testCalculateFine_ZeroDays_ReturnZero() {
        assertEquals(0.0, fineService.calculateFine(0));
    }

    @Test
    @DisplayName("TC002 - Tính phí 1 ngày - trả về 2000")
    void testCalculateFine_OneDay_Return2000() {
        assertEquals(2000.0, fineService.calculateFine(1));
    }

    @Test
    @DisplayName("TC003 - Tính phí 3 ngày - trả về 6000")
    void testCalculateFine_ThreeDays_Return6000() {
        assertEquals(6000.0, fineService.calculateFine(3));
    }

    @Test
    @DisplayName("TC004 - Tính phí số âm - trả về 0")
    void testCalculateFine_NegativeDays_ReturnZero() {
        assertEquals(0.0, fineService.calculateFine(-5));
    }

    @Test
    @DisplayName("TC005 - Đóng phạt thành công")
    void testPayFine_ValidId_Success() {
        when(fineRepository.findById(1L)).thenReturn(Optional.of(testFine));
        when(fineRepository.save(any(Fine.class))).thenReturn(testFine);

        Fine result = fineService.payFine(1L);

        assertTrue(result.isPaid());
        verify(fineRepository).save(any(Fine.class));
    }

    @Test
    @DisplayName("TC006 - Lấy phạt chưa đóng")
    void testGetUnpaidFines_ReturnUnpaidList() {
        List<Fine> unpaidFines = Arrays.asList(testFine);
        when(fineRepository.findByPaid(false)).thenReturn(unpaidFines);

        List<Fine> result = fineService.getUnpaidFines();

        assertEquals(1, result.size());
        assertFalse(result.get(0).isPaid());
    }

    @Test
    @DisplayName("TC007 - Lấy chi tiết phạt theo ID")
    void testGetFineById_ValidId_ReturnFine() {
        when(fineRepository.findById(1L)).thenReturn(Optional.of(testFine));

        Fine result = fineService.getFineById(1L);

        assertNotNull(result);
        assertEquals(6000.0, result.getAmount());
    }

    @Test
    @DisplayName("TC008 - Lấy tất cả phạt")
    void testGetAllFines_ReturnAll() {
        List<Fine> allFines = Arrays.asList(testFine);
        when(fineRepository.findAll()).thenReturn(allFines);

        List<Fine> result = fineService.getAllFines();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("TC009 - Tính tổng phạt theo tháng")
    void testGetTotalFinesByMonth_ValidMonth_ReturnTotal() {
        when(fineRepository.findAll()).thenReturn(Arrays.asList(testFine));

        double result = fineService.getTotalFinesByMonth(LocalDate.now().getYear(), LocalDate.now().getMonthValue());

        assertEquals(6000.0, result);
    }

    @Test
    @DisplayName("TC010 - Tính tổng phạt chưa đóng")
    void testGetTotalUnpaidAmount_ReturnTotal() {
        when(fineRepository.findByPaid(false)).thenReturn(Arrays.asList(testFine));

        double result = fineService.getTotalUnpaidAmount();

        assertEquals(6000.0, result);
    }
}
