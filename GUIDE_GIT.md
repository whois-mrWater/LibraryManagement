## Branch trong Git là gì?

Hình dung project như **1 cuốn sách đang viết chung**. Nếu 4 người cùng viết vào 1 bản → loạn! Branch là cách mỗi người **tách ra 1 bản nháp riêng**, viết xong mới ghép lại.[1]

```
main (bản chính)
  ├── feature/books     ← Person A đang làm ở đây
  ├── feature/members   ← Person B đang làm ở đây
  ├── feature/borrow    ← Person C đang làm ở đây
  └── feature/fines     ← Person D đang làm ở đây
```

***

## Lệnh mỗi người cần chạy 1 lần duy nhất lúc bắt đầu

**Person B:**
```bash
git checkout main          # Về nhánh chính trước
git pull origin main       # Lấy code mới nhất (có code của anh)
git checkout -b feature/members  # Tạo nhánh mới và chuyển sang
```

**Person C:**
```bash
git checkout main
git pull origin main
git checkout -b feature/borrow
```

**Person D:**
```bash
git checkout main
git pull origin main
git checkout -b feature/fines
```

***

## Làm xong thì push lên như thế nào?

```bash
git add .
git commit -m "Hoàn thành MemberController và HTML"
git push origin feature/members   # Push lên nhánh của mình
```

Sau đó lên GitHub tạo **Pull Request** → anh hoặc team review → merge vào `main`.

***

## Các thành viên cần nhớ

không push thẳng vào `main`:

```bash
git checkout -b feature/books     # Tạo nhánh cho bản thân
# ... làm việc ...
git push origin feature/books
```

***

## Tóm tắt quy trình

```
1. git checkout main          → về main
2. git pull origin main       → lấy code mới nhất
3. git checkout -b feature/xxx → tạo nhánh mới
4. Code...
5. git add . + git commit     → lưu lại
6. git push origin feature/xxx → đẩy lên
7. Tạo Pull Request trên GitHub → merge vào main
```

> ⚠️ **Quan trọng:** Không ai push thẳng vào `main` — chỉ merge qua Pull Request để tránh conflict! 🚀
