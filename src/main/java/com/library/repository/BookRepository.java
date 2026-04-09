package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookCode(String bookCode);

    List<Book> findByCategory(String category);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    // Tự động cập nhật available_copies từ borrow_records
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE books SET available_copies = (
                total_copies - (
                    SELECT COUNT(br.id)
                    FROM borrow_records br
                    WHERE br.book_id = books.id
                    AND br.status IN ('BORROWED', 'OVERDUE')
                )
            )
            """, nativeQuery = true)
    void recalculateAvailableCopies();

}
