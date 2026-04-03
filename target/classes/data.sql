-- ============================================================
-- TEST DATA - Library Management System
-- ============================================================

-- ============= BOOKS DATA (10 cuốn sách) =============
INSERT INTO books (book_code, title, author, publisher, category, total_copies, available_copies) VALUES
('BK001', 'Dế Mèn Phiêu Lưu Ký', 'Tô Hoài', 'Kim Đồng', 'Văn học trẻ em', 10, 10),
('BK002', 'Cho Tôi Xin Một Vé Đi Tuổi Thơ', 'Nguyễn Nhật Ánh', 'Trẻ', 'Văn học trẻ em', 8, 8),
('BK003', 'Tôi Thấy Hoa Vàng Trên Cỏ Xanh', 'Nguyễn Nhật Ánh', 'Trẻ', 'Văn học trẻ em', 12, 12),
('BK004', 'Mắt Biếc', 'Nguyễn Nhật Ánh', 'Trẻ', 'Văn học trẻ em', 7, 7),
('BK005', 'Lão Hạc', 'Nam Cao', 'Văn Học', 'Văn học', 6, 6),
('BK006', 'Tắt Đèn', 'Ngô Tất Tố', 'Văn Học', 'Văn học', 5, 5),
('BK007', 'Số Đỏ', 'Vũ Trọng Phụng', 'Văn Học', 'Văn học', 9, 9),
('BK008', 'Truyện Kiều', 'Nguyễn Du', 'Giáo Dục', 'Văn học cổ điển', 11, 11),
('BK009', 'Nhà Giả Kim', 'Paulo Coelho', 'Hội Nhà Văn', 'Phát triển cá nhân', 10, 10),
('BK010', 'Đắc Nhân Tâm', 'Dale Carnegie', 'Trẻ', 'Phát triển cá nhân', 14, 14);

-- ============= MEMBERS DATA (Test Users) =============
INSERT INTO members (member_code, full_name, phone, email, join_date, active) VALUES
('M001', 'Nguyễn Văn A', '0901234567', 'nguyenvana@email.com', '2026-01-01', true),
('M002', 'Trần Thị B', '0912345678', 'tranthib@email.com', '2026-01-15', true),
('M003', 'Lê Minh C', '0923456789', 'leminhc@email.com', '2026-02-01', true),
('M004', 'Phạm Quốc D', '0934567890', 'phamquocd@email.com', '2026-02-10', false),
('M005', 'Hoàng Tuyết E', '0945678901', 'hoangtuyet@email.com', '2026-02-20', true);

-- ============= BORROW RECORDS DATA =============
INSERT INTO borrow_records (member_id, book_id, borrow_date, due_date, return_date, status) VALUES
(1, 1, '2026-03-01', '2026-03-15', NULL, 'BORROWED'),
(2, 2, '2026-03-05', '2026-03-19', '2026-03-18', 'RETURNED'),
(3, 3, '2026-03-10', '2026-03-24', NULL, 'BORROWED'),
(1, 5, '2026-02-01', '2026-02-15', '2026-02-20', 'RETURNED'),
(5, 8, '2026-03-15', '2026-03-29', NULL, 'BORROWED');

-- ============= FINES DATA =============
INSERT INTO fines (borrow_record_id, overdue_days, amount, fine_date, paid) VALUES
(2, 0, 0, '2026-03-18', false),
(4, 5, 50000, '2026-02-20', true);

-- ============= RESERVATIONS DATA =============
INSERT INTO reservations (member_id, book_id, reservation_date, status) VALUES
(2, 6, '2026-03-20', 'ACTIVE'),
(3, 7, '2026-03-21', 'ACTIVE'),
(4, 1, '2026-03-22', 'CANCELLED');

-- ============================================================
-- End of Test Data
-- ============================================================
