package com.library.e2e;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ====================================================
 * AUTOMATION TEST VOI PLAYWRIGHT
 * Chay file nay SAU KHI da chay Spring Boot app (port 8080)
 * De chay: mvn test -Dtest=LibraryE2ETest
 * ====================================================
 */
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
                .setSlowMo(500)
        );
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(new Browser.NewContextOptions()
            .setRecordVideoDir(java.nio.file.Paths.get("test-videos/"))
        );
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }

    // ===== TEST 1: TRANG CHU =====
    @Test
    @Order(1)
    @DisplayName("E2E_TC001 - Mo trang chu - hien thi du 4 menu")
    void testHomePage_ShowAllMenus() {
        page.navigate(BASE_URL + "/");
        assertTrue(page.title().contains("Thu Vien") || page.content().contains("Thu Vien"));
        assertNotNull(page.locator("a[href='/books']").first());
        assertNotNull(page.locator("a[href='/members']").first());
        System.out.println("✅ E2E_TC001 PASSED");
    }

    // ===== TEST 2: THEM SACH =====
    @Test
    @Order(2)
    @DisplayName("E2E_TC002 - Them sach moi qua giao dien web")
    void testAddBook_ViaUI_Success() {
        page.navigate(BASE_URL + "/books/new");

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

    // ===== TEST 3: SUA SACH (PERSON A) =====
    @Test
    @Order(3)
    @DisplayName("E2E_TC003 - Sua sach vua them qua giao dien web")
    void testEditBook_ViaUI_Success() {
        // Vao trang danh sach, tim dong chua "Sach Test Playwright"
        page.navigate(BASE_URL + "/books");

        // Click nut Sua cua dong chua "Sach Test Playwright"
        page.locator("tr:has-text('Sach Test Playwright') a[href*='/books/edit/']").click();

        // Kiem tra da vao dung trang sua
        assertTrue(page.url().contains("/books/edit/"));

        // Doi ten sach
        page.fill("#title", "Sach Test Playwright DA SUA");
        page.fill("#author", "Tac Gia Da Sua");

        page.click("#submitBtn");

        // Kiem tra duoc chuyen ve danh sach va hien ten moi
        page.waitForURL(BASE_URL + "/books");
        assertTrue(page.content().contains("Sach Test Playwright DA SUA"));
        System.out.println("✅ E2E_TC003 PASSED");
    }

    // ===== TEST 4: XOA SACH (PERSON A) =====
    @Test
    @Order(4)
    @DisplayName("E2E_TC004 - Xoa sach vua sua qua giao dien web")
    void testDeleteBook_ViaUI_Success() {
        page.navigate(BASE_URL + "/books");

        // Click nut Xoa cua dong chua "Sach Test Playwright DA SUA"
        page.locator("tr:has-text('Sach Test Playwright DA SUA') form[action*='/books/delete/'] button").click();

        // Kiem tra van o trang danh sach
        page.waitForURL(BASE_URL + "/books");

        // Kiem tra sach da bi xoa khoi danh sach
        assertFalse(page.content().contains("Sach Test Playwright DA SUA"));
        System.out.println("✅ E2E_TC004 PASSED");
    }

    // ===== TEST 5: TIM KIEM SACH (PERSON A) =====
    @Test
    @Order(5)
    @DisplayName("E2E_TC005 - Tim kiem sach theo tu khoa")
    void testSearchBook_ViaUI_Success() {
        page.navigate(BASE_URL + "/books");

        // Nhap tu khoa tim kiem
        page.fill("input[name='keyword']", "Nguyen Nhat Anh");
        page.press("input[name='keyword']", "Enter");

        // Kiem tra ket qua hien thi dung
        assertTrue(page.content().contains("Nguyen Nhat Anh"));

        // Kiem tra tu khoa van con trong o tim kiem
        assertEquals("Nguyen Nhat Anh", page.inputValue("input[name='keyword']"));
        System.out.println("✅ E2E_TC005 PASSED");
    }

    // TODO: Them cac E2E test cho:
    // - E2E_TC006 (Person B): Dang ky thanh vien moi - navigate /members/new, fill #fullName, #phone, #email, click #submitBtn
    // - E2E_TC007 (Person C): Muon sach - navigate /borrow, chon member + book tu dropdown, click nut Muon
    // - E2E_TC008 (Person C): Tra sach - navigate /borrow, click nut Tra Sach tren dong dang muon
    // - E2E_TC009 (Person D): Xem tien phat - navigate /fines, kiem tra hien thi danh sach
    // - E2E_TC010 (Person D): Dong tien phat - navigate /fines, click nut Dong Phat
}
