# 📚 He Thong Quan Ly Thu Vien - Library Management System
## Mon: Kiem Thu Phan Mem | Nhom: A, B, C, D

---

## 🏗️ Cau Truc Du An

```
LibraryManagement/
├── src/main/java/com/library/
│   ├── model/          # Entity classes (A, B, C, D moi nguoi 1 class)
│   ├── repository/     # Database access
│   ├── service/        # Business logic (MOI NGUOI VIET PHAN CUA MINH O DAY)
│   ├── controller/     # HTTP endpoints
│   └── LibraryApp.java # Diem khoi dong
├── src/main/resources/
│   └── templates/      # Giao dien HTML
├── src/test/java/com/library/
│   ├── service/        # JUnit Tests (MOI NGUOI VIET TEST CUA MINH O DAY)
│   └── e2e/            # Playwright Automation Tests
└── pom.xml
```

---

## 👥 Phan Cong Nhom

| Thanh vien | Chuc nang | File can sua |
|---|---|---|
| **A** | Quan ly Sach (CRUD, tim kiem) | `BookService.java`, `BookServiceTest.java` |
| **B** | Quan ly Thanh vien (dang ky, cap nhat) | `MemberService.java`, `MemberServiceTest.java` |
| **C** | Muon / Tra Sach | `BorrowService.java`, `BorrowServiceTest.java` |
| **D** | Quan ly Tien Phat + Thong ke | `FineService.java`, `FineServiceTest.java` |

---

## 🚀 Cach Chay Du An

```bash
# 1. Cai Maven va Java 17
# 2. Clone project tu GitHub
git clone https://github.com/[ten-nhom]/LibraryManagement.git
cd LibraryManagement

# 3. Chay ung dung
mvn spring-boot:run

# 4. Mo trinh duyet: http://localhost:8080

# 5. Chay JUnit Tests
mvn test

# 6. Chay Playwright E2E (can chay app truoc)
mvn test -Dtest=LibraryE2ETest
```

---

## 📋 Quy Uoc Lam Viec Nhom (Git)

- Moi nguoi lam tren branch rieng: `feature/thanh-vien-A`, `feature/thanh-vien-B`...
- Khong push thang len `main`
- Tao Pull Request de merge vao `main`
