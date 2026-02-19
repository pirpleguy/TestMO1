package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import java.nio.file.Paths;

public abstract class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected LoginPage loginPage;

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

        loginPage = new LoginPage(page);
    }

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
            System.out.println("Video saved");
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    protected void printHeader(String testName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(testName);
        System.out.println("=".repeat(60));
    }
}