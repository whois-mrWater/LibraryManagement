-- ============================================================
-- FINES DATA - Tiền phạt
-- (Load SAU borrow_records)
-- ============================================================

INSERT INTO fines (borrow_record_id, overdue_days, amount, fine_date, paid) VALUES
(2, 0,     0, '2026-03-18', false),
(4, 5, 50000, '2026-02-20', true);
