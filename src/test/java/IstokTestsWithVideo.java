import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;

public class IstokTestsWithVideo {

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(1000));

        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos"))
                .setRecordVideoSize(1280, 720));

        page = context.newPage();
        page.setDefaultTimeout(60000);
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
            System.out.println("✅ Video saved");
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    void login() {
        System.out.println("\n🔐 LOGIN PROCESS");
        System.out.println("1. Opening website...");
        page.navigate("https://test.ppma.ru/portal");
        page.waitForLoadState();
        page.waitForTimeout(2000);

        System.out.println("2. Entering login...");
        page.locator("#username").waitFor();
        page.fill("#username", "TestMO1");
        page.waitForTimeout(1000);

        System.out.println("3. Entering password...");
        page.fill("#password", "AEJPx7BY7OJOBF2K");
        page.waitForTimeout(1000);

        System.out.println("4. Clicking login button...");
        page.click("button[type='submit']");
        page.waitForLoadState();
        page.waitForTimeout(3000);

        System.out.println("✅ Login successful\n");
    }

    String getCellText(int index) {
        try {
            return page.locator("tbody tr:first-child td:nth-child(" + index + ")").first().textContent().trim();
        } catch (Exception e) {
            return "N/A";
        }
    }

    @Test
    @DisplayName("TC001: Device Search")
    void testSearchDevice() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 TC-001: Search device 'htkwv'");
        System.out.println("=".repeat(60));

        try {
            login();

            // Click on Устройства ПМП (index 1)
            System.out.println("📌 Clicking on 'Устройства ПМП' menu (index 1)");
            page.locator(".ant-menu-item").nth(1).click();
            page.waitForLoadState();
            page.waitForTimeout(3000);

            // Используем data-class="pmp-search" для поисковой строки
            System.out.println("🔍 Waiting for search input with data-class='pmp-search'...");
            page.locator("input[data-class='pmp-search']").first().waitFor();
            page.waitForTimeout(1000);

            // Search for device
            System.out.println("🔎 Searching for: htkwv");
            page.locator("input[data-class='pmp-search']").first().fill("htkwv");
            page.waitForTimeout(1000);

            // Нажимаем кнопку поиска (иконка поиска)
            System.out.println("🔘 Clicking search button...");
            page.locator("button.ant-input-search-button").first().click();
            page.waitForTimeout(3000);

            // Check results
            boolean deviceFound = page.locator("text=htkwv").count() > 0;

            if (deviceFound) {
                System.out.println("✅ Device found!");
                System.out.println("\n📋 Device details:");
                System.out.println("  Serial number: " + getCellText(2));
                System.out.println("  Model: " + getCellText(4));
                System.out.println("  Status: " + getCellText(11));

                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("tc001-success.png"))
                        .setFullPage(true));

                System.out.println("\n✅ TC-001: PASSED");
            } else {
                System.out.println("❌ Device not found!");
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("tc001-failed.png")));
                Assertions.fail("Device htkwv not found");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("tc001-error.png")));
            throw e;
        }
    }

    @Test
    @DisplayName("TC002: Monitoring Program Search")
    void testMonitoringProgram() {
        String orderId = "c134009d-99d4-4958-a7a7-7b3ddd3dbef1";

        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 TC-002: Search monitoring program by ID");
        System.out.println("=".repeat(60));

        try {
            login();

            // Click on Программы мониторинга (index 2)
            System.out.println("📌 Clicking on 'Программы мониторинга' menu (index 2)");
            page.locator(".ant-menu-item").nth(2).click();
            page.waitForLoadState();
            page.waitForTimeout(3000);

            // Используем data-class="pmp-search" для поисковой строки
            System.out.println("🔍 Waiting for search input with data-class='pmp-search'...");
            page.locator("input[data-class='pmp-search']").first().waitFor();
            page.waitForTimeout(1000);

            // Search by order ID
            System.out.println("🔎 Searching by order ID: " + orderId);
            page.locator("input[data-class='pmp-search']").first().fill(orderId);
            page.waitForTimeout(1000);

            // Нажимаем кнопку поиска (иконка поиска)
            System.out.println("🔘 Clicking search button...");
            page.locator("button.ant-input-search-button").first().click();
            page.waitForTimeout(4000);

            // Check results
            boolean programFound = page.locator("text=" + orderId.substring(0, 8)).count() > 0;

            if (programFound) {
                System.out.println("✅ Program found!");
                System.out.println("\n📋 Program details:");
                System.out.println("  Order ID: " + getCellText(4));
                System.out.println("  Status: " + getCellText(5));
                System.out.println("  Device serial: " + getCellText(8));

                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("tc002-success.png"))
                        .setFullPage(true));

                System.out.println("\n✅ TC-002: PASSED");
            } else {
                System.out.println("❌ Program not found!");
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("tc002-failed.png")));
                Assertions.fail("Program with order ID " + orderId + " not found");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("tc002-error.png")));
            throw e;
        }
    }

    @Test
    @DisplayName("TC003: Subscription Search")
    void testSubscription() {
        String subscriptionId = "a4947e2d-7865-4cb9-9d03-3e0649426ef1";

        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 TC-003: Search subscription by ID");
        System.out.println("=".repeat(60));

        try {
            login();

            // Click on Подписки (index 3)
            System.out.println("📌 Clicking on 'Подписки' menu (index 3)");
            page.locator(".ant-menu-item").nth(3).click();
            page.waitForLoadState();
            page.waitForTimeout(3000);

            // Click advanced search button using data-class="pmp-button-search"
            System.out.println("🔍 Looking for advanced search button with data-class='pmp-button-search'...");
            page.locator("button[data-class='pmp-button-search']").first().waitFor();
            page.locator("button[data-class='pmp-button-search']").first().click();
            System.out.println("   Clicked button with data-class='pmp-button-search'");
            page.waitForTimeout(2000);

            // Ждем появления модального окна
            System.out.println("⏳ Waiting for modal dialog to appear...");
            page.locator("div.ant-modal[role='dialog']").first().waitFor();
            page.waitForTimeout(1000);

            // В модальном окне ищем поле "Идентификатор" по id="id"
            System.out.println("📝 Entering subscription ID in 'Идентификатор' field...");
            page.locator("input#id").first().waitFor();
            page.locator("input#id").first().fill(subscriptionId);
            System.out.println("   Entered ID: " + subscriptionId);
            page.waitForTimeout(1000);

            // Нажимаем кнопку OK в модальном окне (type="submit")
            System.out.println("✅ Clicking OK button in modal...");
            page.locator("button[type='submit']").filter(new Locator.FilterOptions().setHasText("OK")).first().waitFor();
            page.locator("button[type='submit']").filter(new Locator.FilterOptions().setHasText("OK")).first().click();
            System.out.println("   Clicked OK button");
            page.waitForTimeout(4000);

            // Check results
            boolean subscriptionFound = page.locator("text=" + subscriptionId.substring(0, 8)).count() > 0;

            if (subscriptionFound) {
                System.out.println("✅ Subscription found!");
                System.out.println("\n📋 Subscription details:");
                System.out.println("  ID: " + getCellText(2));
                System.out.println("  Organization: " + getCellText(3));
                System.out.println("  Status: " + getCellText(7));
                System.out.println("  Type: " + getCellText(9));

                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("tc003-success.png"))
                        .setFullPage(true));

                System.out.println("\n✅ TC-003: PASSED");
            } else {
                System.out.println("❌ Subscription not found!");
                page.screenshot(new Page.ScreenshotOptions()
                        .setPath(Paths.get("tc003-failed.png")));
                Assertions.fail("Subscription with ID " + subscriptionId + " not found");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("tc003-error.png")));
            throw e;
        }
    }
}