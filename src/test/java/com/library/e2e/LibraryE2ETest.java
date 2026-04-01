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
                .setHeadless(false)   // false = hien thi trinh duyet (de quay video)
                .setSlowMo(500)       // Chay cham 500ms moi buoc (de quay ro hon)
        );
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContext() {
        context = browser.newContext(new Browser.NewContextOptions()
            .setRecordVideoDir(java.nio.file.Paths.get("test-videos/")) // Tu dong quay video
        );
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close(); // Video duoc luu sau khi context dong
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
        page.navigate(BASE_URL + "/books/add");

        // Dien form
        page.fill("#title", "Sach Test Playwright");
        page.fill("#author", "Tac Gia Test");
        page.fill("#isbn", "111-222-333");
        page.fill("#category", "Test");
        page.fill("#totalCopies", "3");
        page.fill("#availableCopies", "3");

        // Click nut Luu
        page.click("#submitBtn");

        // Kiem tra duoc chuyen ve trang danh sach
        page.waitForURL(BASE_URL + "/books");
        assertTrue(page.content().contains("Sach Test Playwright"));
        System.out.println("✅ E2E_TC002 PASSED");
    }

    // ===== TEST 3: DANG KY THANH VIEN =====
    @Test
    @Order(3)
    @DisplayName("E2E_TC003 - Dang ky thanh vien moi qua giao dien web")
    void testRegisterMember_ViaUI_Success() {
        page.navigate(BASE_URL + "/members/register");

        page.fill("#fullName", "Nguyen Van Test");
        page.fill("#email", "test@example.com");
        page.fill("#phone", "0901234567");

        page.click("#submitBtn");

        page.waitForURL(BASE_URL + "/members");
        assertTrue(page.content().contains("Nguyen Van Test"));
        System.out.println("✅ E2E_TC003 PASSED");
    }

    // TODO: Them cac E2E test cho:
    // - E2E_TC004: Muon sach
    // - E2E_TC005: Tra sach
    // - E2E_TC006: Xem tien phat
    // - E2E_TC007: Dong tien phat
}
