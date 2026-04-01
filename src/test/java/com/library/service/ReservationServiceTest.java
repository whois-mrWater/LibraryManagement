package com.library.service;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Reservation;
import com.library.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * PERSON 3 - Reservation Service Unit Tests
 * 🎫 Test chức năng đặt sách
 * 
 * Test Cases (9 cases):
 * 1. makeReservation - Tạo đặt sách thành công
 * 2. makeReservation - Null member
 * 3. makeReservation - Null book
 * 4. cancelReservation - Hủy thành công
 * 5. cancelReservation - ID không tồn tại
 * 6. getReservationsByMember - Lấy danh sách đặt của thành viên
 * 7. getActiveReservations - Lấy tất cả đặt ACTIVE
 * 8. getReservationById - Lấy chi tiết đặt
 * 9. deleteReservation - Xoá đặt
 */
@DisplayName("Reservation Service Tests")
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Member testMember;
    private Book testBook;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testMember = new Member();
        testMember.setId(1L);
        testMember.setMemberCode("MEM001");
        testMember.setFullName("Nguyễn Văn A");
        testMember.setPhone("09123456789");
        testMember.setActive(true);

        testBook = new Book();
        testBook.setId(1L);
        testBook.setBookCode("B001");
        testBook.setTitle("Dế Mèn Phiêu Lưu Ký");
        testBook.setAuthor("Tô Hoài");
        testBook.setPublisher("Nxb Văn học");
        testBook.setTotalCopies(5);
        testBook.setAvailableCopies(3);

        testReservation = new Reservation();
        testReservation.setId(1L);
        testReservation.setMember(testMember);
        testReservation.setBook(testBook);
        testReservation.setReservationDate(LocalDate.now());
        testReservation.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("TC1: Tạo đặt sách thành công")
    void testMakeReservationSuccess() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.makeReservation(testMember, testBook);

        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(testMember.getId(), result.getMember().getId());
        assertEquals(testBook.getId(), result.getBook().getId());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("TC2: Tạo đặt với member null")
    void testMakeReservationWithNullMember() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.makeReservation(null, testBook);
        });
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC3: Tạo đặt với book null")
    void testMakeReservationWithNullBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.makeReservation(testMember, null);
        });
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC4: Hủy đặt sách thành công")
    void testCancelReservationSuccess() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        boolean result = reservationService.cancelReservation(1L);

        assertTrue(result);
        assertEquals("CANCELLED", testReservation.getStatus());
        assertNotNull(testReservation.getCancelledDate());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("TC5: Hủy đặt không tồn tại")
    void testCancelReservationNotFound() {
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = reservationService.cancelReservation(999L);

        assertFalse(result);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC6: Lấy danh sách đặt của thành viên")
    void testGetReservationsByMember() {
        Reservation res2 = new Reservation();
        res2.setId(2L);
        res2.setMember(testMember);
        res2.setBook(testBook);
        res2.setStatus("ACTIVE");

        List<Reservation> reservations = Arrays.asList(testReservation, res2);
        when(reservationRepository.findByMember(testMember)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationsByMember(testMember);

        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findByMember(testMember);
    }

    @Test
    @DisplayName("TC7: Lấy tất cả đặt ACTIVE")
    void testGetActiveReservations() {
        List<Reservation> activeReservations = Arrays.asList(testReservation);
        when(reservationRepository.findByStatus("ACTIVE")).thenReturn(activeReservations);

        List<Reservation> result = reservationService.getActiveReservations();

        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
        verify(reservationRepository, times(1)).findByStatus("ACTIVE");
    }

    @Test
    @DisplayName("TC8: Lấy chi tiết đặt sách")
    void testGetReservationById() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        Reservation result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(testMember.getId(), result.getMember().getId());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TC9: Xoá đặt sách")
    void testDeleteReservation() {
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).deleteById(1L);
    }
}
