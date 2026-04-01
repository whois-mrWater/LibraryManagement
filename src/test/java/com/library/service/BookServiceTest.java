package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * PERSON 1 - Book Service Unit Tests
 */
@DisplayName("Book Service Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setBookCode("TB001");
        testBook.setPublisher("Test Pub");
        testBook.setCategory("Fiction");
        testBook.setTotalCopies(5);
        testBook.setAvailableCopies(3);
    }

    @Test
    @DisplayName("TC001 - Lấy tất cả sách")
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TC002 - Lấy sách theo ID")
    void testGetBookById() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }

    @Test
    @DisplayName("TC003 - Thêm sách mới")
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book result = bookService.addBook(testBook);

        assertNotNull(result);
        assertEquals("TB001", result.getBookCode());
    }

    @Test
    @DisplayName("TC004 - Cập nhật sách")
    void testUpdateBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        testBook.setTitle("Updated Title");
        Book result = bookService.updateBook(1L, testBook);

        assertNotNull(result);
    }

    @Test
    @DisplayName("TC005 - Xoá sách")
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("TC006 - Tìm sách theo tiêu đề")
    void testSearchByTitle() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(books);

        List<Book> result = bookService.searchByTitle("Test");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("TC007 - Tìm sách theo tác giả")
    void testSearchByAuthor() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findByAuthorContainingIgnoreCase("Author")).thenReturn(books);

        List<Book> result = bookService.searchByAuthor("Author");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("TC008 - Tìm sách theo từ khóa")
    void testSearchBooks() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase("Test", "Test")).thenReturn(books);

        List<Book> result = bookService.searchBooks("Test");

        assertEquals(1, result.size());
    }
}
