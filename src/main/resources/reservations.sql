-- ============================================================
-- RESERVATIONS DATA - Đặt trước sách
-- (Load SAU members + books)
-- ============================================================

INSERT INTO reservations (member_id, book_id, reservation_date, status) VALUES
(2, 6, '2026-03-20', 'ACTIVE'),
(3, 7, '2026-03-21', 'ACTIVE'),
(4, 1, '2026-03-22', 'CANCELLED');
