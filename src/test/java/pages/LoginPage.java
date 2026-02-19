package pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    // Locators
    private final String USERNAME_FIELD = "#username";
    private final String PASSWORD_FIELD = "#password";
    private final String LOGIN_BUTTON = "button[type='submit']";

    public LoginPage(Page page) {
        super(page);
    }

    public LoginPage open() {
        System.out.println("1. Opening website...");
        page.navigate("https://test.ppma.ru/portal");
        waitForLoadState();
        waitForTimeout(2000);
        return this;
    }

    private void waitForLoadState() {
        page.waitForLoadState();
    }

    public LoginPage enterUsername(String username) {
        System.out.println("2. Entering login...");
        waitForElement(USERNAME_FIELD);
        fill(USERNAME_FIELD, username);
        waitForTimeout(1000);
        return this;
    }

    public LoginPage enterPassword(String password) {
        System.out.println("3. Entering password...");
        fill(PASSWORD_FIELD, password);
        waitForTimeout(1000);
        return this;
    }

    public DevicesPage clickLoginButton() {
        System.out.println("4. Clicking login button...");
        click(LOGIN_BUTTON);
        waitForLoadState();
        waitForTimeout(3000);
        System.out.println("Login successful\n");
        return new DevicesPage(page);
    }

    // Business method
    public DevicesPage loginAs(String username, String password) {
        return open()
                .enterUsername(username)
                .enterPassword(password)
                .clickLoginButton();
    }
}