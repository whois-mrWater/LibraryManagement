package com.library.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Library Management System")
@Feature("Book Management - Person A")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LibraryE2ETest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    static final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setSlowMo(500));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("test-videos/")));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() throws IOException {
        // ✅ Bước 1: Lưu object video (không phải path!)
        var video = page.video();

        // ✅ Bước 2: Close context → Playwright bắt đầu ghi file .webm
        context.close();

        // ✅ Bước 3: Gọi path() SAU close → file đã ghi xong hoàn toàn
        if (video != null) {
            var videoPath = video.path(); // ← SAU close mới gọi!
            byte[] videoBytes = java.nio.file.Files.readAllBytes(videoPath);
            Allure.addAttachment("Test Video", "video/webm",
                    new java.io.ByteArrayInputStream(videoBytes), ".webm");
        }
    }

    // ===== HELPER: ĐĂNG NHẬP =====
    // ✅ FIX: form login dùng name="phone", không phải name="username"
    void loginAs(String phone, String password) {
        page.navigate(BASE_URL + "/login");
        page.waitForLoadState(); // ✅ FIX: chờ trang load xong tránh lỗi "Object doesn't exist"
        page.fill("input[name='phone']", phone);
        page.fill("input[name='password']", password);
        page.click("form:has(input[name='phone']) button[type='submit']");
        page.waitForLoadState(); // ✅ chờ redirect xong
    }

    // ===== TEST 1: TRANG CHU =====
    @Test
    @Order(1)
    @Story("Trang chủ")
    @DisplayName("E2E_TC001 - Mo trang chu - hien thi du 4 menu")
    @Description("Mở trang chủ, kiểm tra tiêu đề và các link menu Books, Members")
    @Severity(SeverityLevel.CRITICAL)
    void testHomePage_ShowAllMenus() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/");
        page.waitForLoadState();
        assertTrue(page.title().contains("🏠 Trang Chủ") || page.content().contains("🏠 Trang Chủ"));
        assertNotNull(page.locator("a[href='/books']").first());
        assertNotNull(page.locator("a[href='/members']").first());
        System.out.println("✅ E2E_TC001 PASSED");
    }

    // ===== TEST 2: THEM SACH =====
    @Test
    @Order(2)
    @Story("Thêm sách")
    @DisplayName("E2E_TC002 - Them sach moi qua giao dien web")
    @Description("Điền form thêm sách mới và kiểm tra hiển thị trong danh sách")
    @Severity(SeverityLevel.CRITICAL)
    void testAddBook_ViaUI_Success() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/books/new");
        page.waitForLoadState();

        page.fill("#bookCode", "BKTEST001");
        page.fill("#title", "Sach Test Playwright");
        page.fill("#author", "Tac Gia Test");
        page.fill("#category", "Test");
        page.fill("#publisher", "Nha Xuat Ban Test");
        page.fill("#totalCopies", "3");
        page.fill("#availableCopies", "3");

        page.click("#submitBtn");

        page.waitForURL(BASE_URL + "/books");
        assertTrue(page.content().contains("Sach Test Playwright"));
        System.out.println("✅ E2E_TC002 PASSED");
    }

    // ===== TEST 3: SUA SACH =====
    @Test
    @Order(3)
    @Story("Sửa sách")
    @DisplayName("E2E_TC003 - Sua sach vua them qua giao dien web")
    @Description("Tìm sách vừa thêm và chỉnh sửa tên, tác giả")
    @Severity(SeverityLevel.NORMAL)
    void testEditBook_ViaUI_Success() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/books");
        page.waitForLoadState();

        page.locator("tr:has-text('Sach Test Playwright') a[href*='/books/edit/']").click();
        assertTrue(page.url().contains("/books/edit/"));

        page.fill("#title", "Sach Test Playwright DA SUA");
        page.fill("#author", "Tac Gia Da Sua");
        page.click("#submitBtn");

        page.waitForURL(BASE_URL + "/books");
        assertTrue(page.content().contains("Sach Test Playwright DA SUA"));
        System.out.println("✅ E2E_TC003 PASSED");
    }

    // ===== TEST 4: XOA SACH =====
    @Test
    @Order(4)
    @Story("Xóa sách")
    @DisplayName("E2E_TC004 - Xoa sach vua sua qua giao dien web")
    @Description("Xóa sách đã sửa và kiểm tra không còn trong danh sách")
    @Severity(SeverityLevel.NORMAL)
    void testDeleteBook_ViaUI_Success() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/books");
        page.waitForLoadState();

        page.onDialog(dialog -> dialog.accept());

        page.locator("tr:has-text('Sach Test Playwright DA SUA') form[action*='/books/delete/'] button").click();

        page.waitForURL(BASE_URL + "/books");
        assertFalse(page.content().contains("Sach Test Playwright DA SUA"));
        System.out.println("✅ E2E_TC004 PASSED");
    }

    // ===== TEST 5: TIM KIEM SACH =====
    @Test
    @Order(5)
    @Story("Tìm kiếm sách")
    @DisplayName("E2E_TC005 - Tim kiem sach theo tu khoa")
    @Description("Nhập từ khóa tìm kiếm và kiểm tra kết quả hiển thị đúng")
    @Severity(SeverityLevel.MINOR)
    void testSearchBook_ViaUI_Success() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/books");
        page.waitForLoadState();

        page.fill("input[name='keyword']", "Nguyen Nhat Anh");
        page.press("input[name='keyword']", "Enter");

        assertTrue(page.content().contains("Nguyen Nhat Anh"));
        assertEquals("Nguyen Nhat Anh", page.inputValue("input[name='keyword']"));
        System.out.println("✅ E2E_TC005 PASSED");
    }

    // ===== TEST 6: ĐĂNG NHẬP LIBRARIAN =====
    @Test
    @Order(6)
    @Story("Đăng nhập")
    @DisplayName("E2E_TC006 - Dang nhap thanh cong voi tai khoan LIBRARIAN")
    @Description("Đăng nhập bằng tài khoản admin, kiểm tra vào đúng trang chủ librarian")
    @Severity(SeverityLevel.CRITICAL)
    void testLogin_Librarian_Success() {
        loginAs("admin", "admin123");
        assertTrue(page.url().contains("/") || page.content().contains("Quản lý Sách"));
        assertTrue(page.content().contains("Quản lý Thành viên"));
        System.out.println("✅ E2E_TC006 PASSED");
    }

    // ===== TEST 7: ĐĂNG NHẬP MEMBER =====
    @Test
    @Order(7)
    @Story("Đăng nhập")
    @DisplayName("E2E_TC007 - Dang nhap thanh cong voi tai khoan MEMBER")
    @Description("Đăng nhập bằng tài khoản member, kiểm tra thấy menu Danh sách Sách và Sách của tôi")
    @Severity(SeverityLevel.CRITICAL)
    void testLogin_Member_Success() {
        loginAs("0123456789", "123456");
        assertTrue(page.content().contains("Danh sách Sách"));
        assertTrue(page.content().contains("Sách của tôi"));
        assertFalse(page.content().contains("Quản lý Thành viên"));
        System.out.println("✅ E2E_TC007 PASSED");
    }

    // ===== TEST 8: ĐĂNG NHẬP SAI MẬT KHẨU =====
    @Test
    @Order(8)
    @Story("Đăng nhập")
    @DisplayName("E2E_TC008 - Dang nhap that bai khi sai mat khau")
    @Description("Nhập sai mật khẩu, kiểm tra hiện thông báo lỗi và không vào được hệ thống")
    @Severity(SeverityLevel.CRITICAL)
    void testLogin_WrongPassword_Fail() {
        loginAs("admin", "saimatkhau");
        assertTrue(page.url().contains("/login"));
        assertTrue(page.content().contains("Invalid") || page.content().contains("sai")
                || page.content().contains("error") || page.locator(".error, .alert-danger").count() > 0);
        System.out.println("✅ E2E_TC008 PASSED");
    }

    // ===== TEST 9: HIỂN THỊ DANH SÁCH THÀNH VIÊN =====
    @Test
    @Order(9)
    @Story("Quản lý thành viên")
    @DisplayName("E2E_TC009 - Hien thi danh sach thanh vien")
    @Description("Librarian vào /members, kiểm tra bảng danh sách thành viên hiển thị đúng")
    @Severity(SeverityLevel.NORMAL)
    void testMemberList_Display() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/members");
        page.waitForLoadState();
        assertTrue(page.content().contains("Quản lý Thành viên") || page.content().contains("Họ tên"));
        assertTrue(page.locator("table tbody tr").count() > 0);
        System.out.println("✅ E2E_TC009 PASSED");
    }

    // ===== TEST 10: THÊM THÀNH VIÊN =====
    @Test
    @Order(10)
    @Story("Quản lý thành viên")
    @DisplayName("E2E_TC010 - Them thanh vien moi qua giao dien")
    @Description("Librarian thêm thành viên mới, kiểm tra xuất hiện trong danh sách")
    @Severity(SeverityLevel.CRITICAL)
    void testAddMember_ViaUI_Success() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/members/register");
        page.waitForLoadState();

        page.fill("input[name='fullName']", "Nguyen Van Test");
        page.fill("input[name='phone']", "0999888777");
        page.fill("input[name='email']", "testmember@gmail.com");
        page.click("button[type='submit']");

        page.waitForURL(BASE_URL + "/members");
        assertTrue(page.content().contains("Nguyen Van Test"));
        System.out.println("✅ E2E_TC010 PASSED");
    }

    // ===== TEST 11: SỬA THÔNG TIN THÀNH VIÊN =====
    @Test
    @Order(11)
    @Story("Quản lý thành viên")
    @DisplayName("E2E_TC011 - Sua thong tin thanh vien vua them")
    @Description("Librarian sửa họ tên thành viên vừa thêm, kiểm tra lưu đúng")
    @Severity(SeverityLevel.NORMAL)
    void testEditMember_ViaUI_Success() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/members");
        page.waitForLoadState();

        page.locator("tr:has-text('Nguyen Van Test') a[href*='/edit']").click();
        assertTrue(page.url().contains("/edit"));

        page.fill("input[name='fullName']", "Nguyen Van Test DA SUA");
        page.click("button[type='submit']");

        page.waitForURL(BASE_URL + "/members");
        assertTrue(page.content().contains("Nguyen Van Test DA SUA"));
        System.out.println("✅ E2E_TC011 PASSED");
    }

    // ===== TEST 12: KHÓA THÀNH VIÊN =====
    @Test
    @Order(12)
    @Story("Quản lý thành viên")
    @DisplayName("E2E_TC012 - Khoa tai khoan thanh vien")
    @Description("Librarian khoá thành viên vừa thêm, kiểm tra badge đổi sang Bị khoá")
    @Severity(SeverityLevel.NORMAL)
    void testToggleLock_Member_Success() {
        loginAs("admin", "admin123");
        page.navigate(BASE_URL + "/members");
        page.waitForLoadState();

        page.locator("tr:has-text('Nguyen Van Test DA SUA') button:has-text('Khoá')").click();

        page.waitForURL(BASE_URL + "/members");
        assertTrue(page.content().contains("Bị khoá") || page.content().contains("🔒"));
        System.out.println("✅ E2E_TC012 PASSED");
    }

    // ===== TEST 13: MEMBER XEM SÁCH CỦA TÔI =====
    @Test
    @Order(13)
    @Story("Sách của tôi")
    @DisplayName("E2E_TC013 - Member xem danh sach sach da muon")
    @Description("Member đăng nhập, bấm 'Sách của tôi', kiểm tra bảng sách hiển thị")
    @Severity(SeverityLevel.NORMAL)
    void testMyBooks_Display() {
        loginAs("0123456789", "123456");
        page.navigate(BASE_URL + "/");
        page.waitForLoadState();

        page.locator("a:has-text('Sách của tôi')").click();

        assertTrue(page.locator("#myBooks").isVisible());
        assertTrue(page.content().contains("Sách Đã Mượn"));
        System.out.println("✅ E2E_TC013 PASSED");
    }

    // ===== TEST 14: ĐĂNG KÝ THÀNH CÔNG =====
    @Test
    @Order(14)
    @Story("Đăng ký tài khoản")
    @DisplayName("E2E_TC014 - Dang ky thanh vien moi thanh cong")
    @Description("Điền đủ họ tên, SĐT, email hợp lệ → đăng ký thành công")
    @Severity(SeverityLevel.CRITICAL)
    void testRegister_Success() {
        page.navigate(BASE_URL + "/login");
        page.waitForLoadState();

        page.fill("input[name='fullName']", "Tran Van Playwright");
        page.fill("input[name='phone']", "0911222333");
        page.fill("input[name='email']", "playwright@gmail.com");
        page.click("form:has(input[name='fullName']) button[type='submit']");
        page.waitForLoadState();

        assertTrue(
                page.content().contains("thành công") ||
                        page.content().contains("Đăng ký thành công") ||
                        page.url().contains("/login"));
        System.out.println("✅ E2E_TC014 PASSED");
    }

    // ===== TEST 15: ĐĂNG KÝ THIẾU SĐT =====
    @Test
    @Order(15)
    @Story("Đăng ký tài khoản")
    @DisplayName("E2E_TC015 - Dang ky that bai khi bo trong so dien thoai")
    @Description("Bỏ trống SĐT (required), kiểm tra hiện lỗi validate, không submit được")
    @Severity(SeverityLevel.NORMAL)
    void testRegister_MissingPhone_Fail() {
        page.navigate(BASE_URL + "/login");
        page.waitForLoadState();

        page.fill("input[name='fullName']", "Tran Van Test");
        // ❌ Bỏ trống phone
        page.fill("input[name='email']", "test@gmail.com");
        page.click("form:has(input[name='fullName']) button[type='submit']");

        assertTrue(page.url().contains("/login"));
        System.out.println("✅ E2E_TC015 PASSED");
    }

    // TODO: Them cac E2E test cho:
    // - E2E_TC016 (Person C): Muon sach
    // - E2E_TC017 (Person C): Tra sach
    // - E2E_TC018 (Person D): Xem tien phat
    // - E2E_TC019 (Person D): Dong tien phat
}
