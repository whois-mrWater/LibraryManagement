Dựa vào tree và những gì anh đã làm, đây là danh sách **còn chưa làm**:

## 📖 1. Thuê sách (MEMBER)
- Controller `POST /member/borrow/{bookId}`
- UI hiển thị **QR ảo** thanh toán
- Sau thanh toán → hiển thị **receipt** với random code (VD: `TTT126`)
- Nút **"Xác nhận OK"** đóng receipt

## 🔄 2. Trả sách
**Luồng ADMIN** (`/borrow/list.html` đã có template):
- Xem danh sách sách đang được mượn
- Nút xác nhận trả trực tiếp, không cần nhập code

**Luồng MEMBER** (nút "Trả sách" trong "Sách của tôi"):
- Form nhập mã code từ receipt (VD: `TTT126`)
- Controller `POST /member/return/{id}`
- Thông báo trả thành công

## 💰 3. Tiền phạt (`fines/list.html` đã có template)
- Tính phạt tự động khi quá hạn trả
- Hiển thị danh sách phạt (LIBRARIAN)
- Chức năng đánh dấu đã thanh toán

## 📋 4. Đặt sách trước (`reservations/form.html` + `list.html` đã có template)
- Danh sách sách kèm số lượng **available còn lại**
- MEMBER đặt trước 1 cuốn
- LIBRARIAN xem danh sách đặt chỗ + xác nhận/hủy

## 🧪 5. Service Tests (đã có file, chưa rõ nội dung)
- `BookServiceTest.java`
- `BorrowServiceTest.java`
- `FineServiceTest.java`
- `MemberServiceTest.java`
- `ReservationServiceTest.java`

***

