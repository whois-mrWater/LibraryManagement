## 🚀 **PHẦN: ĐƯA PROJECT LÊN GITHUB**

### **Bước 1: Tạo Repository GitHub**

1. Vào https://github.com/new
2. Nhập tên: `LibraryManagement`
3. Nhập mô tả: `Library Management System with Java 25 & Spring Boot 3.5.0`
4. Chọn **Public** hoặc **Private**
5. Click "Create repository"

**Sẽ nhận được URL**: `https://github.com/your-username/LibraryManagement.git`

---

### **Bước 2: Khởi Tạo Git Trong Project (Nếu Chưa Có)**

```bash
# Vào thư mục project
cd /Users/alanhjhj/Downloads/LibraryManagement

# Khởi tạo git
git init

# Thêm remote (thay your-username bằng username GitHub của anh)
git remote add origin https://github.com/your-username/LibraryManagement.git

# Verify
git remote -v
```

---

### **Bước 3: Add Tất Cả File Vào Git**

```bash
# Add tất cả file
git add .

# Kiểm tra file được add chưa
git status
```

---

### **Bước 4: Commit Lần Đầu**

```bash
git commit -m "Initial commit: LibraryManagement with Java 25, Spring Boot 3.5.0

- Models: Book, Member, BorrowRecord, Fine, Reservation
- Services: BookService, MemberService, BorrowService, FineService, ReservationService
- Repositories: All setup with custom queries
- Test data: 10 books, 5 members, transaction records
- Configuration: H2 database, data.sql auto-load
- Team structure: 5 persons with assigned features"
```

---

### **Bước 5: Push Lên GitHub**

```bash
# Push branch main
git branch -M main
git push -u origin main
```

**Output khi thành công:**
```
Enumerating objects: 50, done.
Counting objects: 100% (50/50), done.
Writing objects: 100% (50/50), 2.15 MiB | 1.5 MiB/s
...
remote: Create a pull request for 'main' on GitHub by visiting:
remote:      https://github.com/your-username/LibraryManagement/pull/new/main
```

---

### **Bước 6: Kiểm Tra Trên GitHub**

Vào: `https://github.com/your-username/LibraryManagement`

Sẽ thấy tất cả file của project!

---

## 👥 **PHẦN: SHARE VỚI TEAM MEMBERS**

### **Cách 1: Cho Phép Access (GitHub Settings)**

1. Vào repo → Settings
2. Collaborators → Add people
3. Thêm GitHub username của team members
4. Choose role: **Collaborator** hoặc **Reviewer**

---

### **Cách 2: Team Members Clone Project**

```bash
# Clone repository
git clone https://github.com/your-username/LibraryManagement.git

# Vào thư mục
cd LibraryManagement

# Tạo branch riêng cho team member (VD: Person A)
git checkout -b feature/person-a-book-management

# Hoặc update branch chính
git pull origin main
```

---

### **Cách 3: Team Members Submit Code (Pull Request)**

**Person A làm xong BookController:**

```bash
# Commit code
git add .
git commit -m "Feature: Implement BookController with 5 endpoints

- GET /api/books: List all books
- POST /api/books: Add new book
- GET /api/books/{id}: Get book detail
- PUT /api/books/{id}: Update book
- DELETE /api/books/{id}: Delete book

Tests: 3 unit tests + E2E tests"

# Push lên GitHub
git push origin feature/person-a-book-management
```

**Rồi tạo Pull Request (PR) trên GitHub:**

1. Vào repo GitHub
2. Click "Compare & pull request"
3. Từ branch `feature/person-a-book-management` vào `main`
4. Nhập description + click "Create pull request"
5. Anh review code → Approve → Merge

---

### **Cách 4: Update Code Mới Nhất**

```bash
# Trước khi code, cập nhật code từ GitHub
git pull origin main

# Hoặc nếu đang ở branch khác
git checkout main
git pull origin main
```

---

## 📋 **Quy Trình Git Cho Team**

### **Workflow Hàng Ngày:**

```bash
# Sáng (Update code mới)
git checkout main
git pull origin main

# Code feature của mình
git checkout -b feature/person-a-[feature-name]
# Và code...

# Trưa (Commit)
git add .
git commit -m "Feature: [description]"
git push origin feature/person-a-[feature-name]

# Chiều (Tạo PR + Review)
# Trên GitHub: Click "Compare & pull request"
# Anh review → Approve → Merge

# Cuối ngày (Update main)
git checkout main
git pull origin main
```

---

## 🔧 **Các Lệnh Git Thường Dùng**

| Lệnh | Tác Dụng |
|------|---------|
| `git status` | Xem status của file |
| `git add .` | Add tất cả file |
| `git commit -m "message"` | Commit với message |
| `git push origin [branch]` | Push lên GitHub |
| `git pull origin main` | Update code từ GitHub |
| `git checkout -b [branch]` | Tạo branch mới |
| `git checkout [branch]` | Switch branch |
| `git log` | Xem history commits |
| `git branch -a` | Xem tất cả branches |

---

## ⚠️ **Lỗi Git Thường Gặp**

### **Lỗi 1: "Permission denied (publickey)"**

```bash
# Cần setup SSH key trước
# Xem hướng dẫn: https://docs.github.com/en/authentication/connecting-to-github-with-ssh

# Hoặc dùng HTTPS + Personal Access Token
git remote set-url origin https://your-token@github.com/your-username/LibraryManagement.git
```

---

### **Lỗi 2: "Conflicts" (Xung Đột Code)**

```bash
# Khi merge xung đột
# Sửa conflict trong code editor
# Rồi:
git add .
git commit -m "Resolve conflicts"
git push origin [branch]
```

---

### **Lỗi 3: "File Too Large"**

```bash
# Xóa file lớn khỏi Git
git rm --cached [file]
git commit -m "Remove large file"
git push
```

---

## 📚 **GitHub Best Practices**

✅ **LÀM:**
- Tạo branch riêng cho mỗi feature
- Commit messages rõ ràng, chi tiết
- Pull request trước khi merge vào main
- Review code của team members

❌ **KHÔNG LÀM:**
- Push trực tiếp vào main
- Commit với message "fix", "update", v.v.
- Quên pull code mới trước khi code
- Commit code k compile được

---