package jreader.test.acceptance;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.ChromeDriverManager;

public final class BrowserManager {

    private static WebDriver browser;

    private BrowserManager() {

    }

    public static void init() {
        if (browser != null) {
            throw new IllegalStateException("Browser should be closed before creating a new one.");
        }

        ChromeDriverManager.getInstance().setup();
        browser = new ChromeDriver();
    }

    public static void close() {
        if (browser == null) {
            throw new IllegalStateException("Browser not initialized yet.");
        }
        browser.close();
        browser = null;
    }

    public static WebDriver getBrowser() {
        return browser;
    }

}
