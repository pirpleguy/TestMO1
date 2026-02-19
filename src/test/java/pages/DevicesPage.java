package pages;

import com.microsoft.playwright.Page;

public class DevicesPage extends BasePage {

    // Locators
    private final String DEVICES_MENU = ".ant-menu-item >> nth=1";
    private final String SEARCH_INPUT = "input[data-class='pmp-search']";
    private final String SEARCH_BUTTON = "button.ant-input-search-button";
    private final String DEVICE_TEXT = "text=%s";

    public DevicesPage(Page page) {
        super(page);
        // Wait for page to load and click devices menu
        waitForElement(".ant-menu-item");
        click(DEVICES_MENU);
        waitForLoadState();
        waitForTimeout(3000);
    }

    private void waitForLoadState() {
        page.waitForLoadState();
    }

    public DevicesPage searchDevice(String deviceName) {
        System.out.println("Waiting for search input...");
        waitForElement(SEARCH_INPUT);
        waitForTimeout(1000);

        System.out.println("Searching for: " + deviceName);
        fill(SEARCH_INPUT, deviceName);
        waitForTimeout(1000);

        System.out.println("Clicking search button...");
        click(SEARCH_BUTTON);
        waitForTimeout(3000);

        return this;
    }

    public boolean isDeviceFound(String deviceName) {
        return page.locator(String.format(DEVICE_TEXT, deviceName)).count() > 0;
    }

    public String getDeviceSerial() {
        return getCellText(2);
    }

    public String getDeviceModel() {
        return getCellText(4);
    }

    public String getDeviceStatus() {
        return getCellText(11);
    }

    public void printDeviceDetails() {
        System.out.println("\nDevice details:");
        System.out.println("  Serial number: " + getDeviceSerial());
        System.out.println("  Model: " + getDeviceModel());
        System.out.println("  Status: " + getDeviceStatus());
    }
}