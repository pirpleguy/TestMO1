package pages;

import com.microsoft.playwright.Page;
import java.nio.file.Paths;

public abstract class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        page.click(selector);
    }

    protected void fill(String selector, String text) {
        page.fill(selector, text);
    }

    protected String getText(String selector) {
        return page.textContent(selector);
    }

    protected boolean isVisible(String selector) {
        return page.isVisible(selector);
    }

    protected void waitForElement(String selector) {
        page.waitForSelector(selector);
    }

    protected void waitForTimeout(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }

    protected String getCellText(int index) {
        try {
            return page.locator("tbody tr:first-child td:nth-child(" + index + ")").first().textContent().trim();
        } catch (Exception e) {
            return "N/A";
        }
    }

    public void takeScreenshot(String name) {
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(name + ".png"))
                .setFullPage(true));
    }
}