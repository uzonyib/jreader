package jreader.test.acceptance.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

import io.github.bonigarcia.wdm.ChromeDriverManager;

@Component
public final class BrowserManager {

    private WebDriver browser;

    public BrowserManager() {
        init();
    }

    public void init() {
        if (browser != null) {
            throw new IllegalStateException("Browser should be closed before creating a new one.");
        }

        ChromeDriverManager.getInstance().setup();
        browser = new ChromeDriver();
    }

    public void close() {
        if (browser == null) {
            throw new IllegalStateException("Browser not initialized yet.");
        }
        browser.close();
        browser = null;
    }

    public WebDriver getBrowser() {
        return browser;
    }

}
