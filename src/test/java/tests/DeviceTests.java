package tests;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.*;
import pages.DevicesPage;
import java.nio.file.Paths;

public class DeviceTests extends BaseTest {

    @Test
    @DisplayName("TC001: Device Search")
    void testSearchDevice() {
        printHeader("TC-001: Search device 'htkwv'");

        try {
            // Login and navigate to Devices page
            DevicesPage devicesPage = loginPage.loginAs("TestMO1", "AEJPx7BY7OJOBF2K");

            // Search for device
            devicesPage.searchDevice("htkwv");

            // Verify results
            if (devicesPage.isDeviceFound("htkwv")) {
                System.out.println("Device found!");
                devicesPage.printDeviceDetails();
                devicesPage.takeScreenshot("tc001-success");
                System.out.println("\nTC-001: PASSED");
            } else {
                System.out.println("Device not found!");
                devicesPage.takeScreenshot("tc001-failed");
                Assertions.fail("Device htkwv not found");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("tc001-error.png")));
            throw e;
        }
    }
}