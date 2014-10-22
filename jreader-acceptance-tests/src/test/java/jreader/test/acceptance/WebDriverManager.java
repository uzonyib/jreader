package jreader.test.acceptance;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class WebDriverManager {
    
    private static WebDriver browser;
    
    private WebDriverManager() {
        
    }
    
    public static void init() {
        if (browser != null) {
            throw new IllegalStateException("Browser should be closed before creating a new one.");
        }
        browser = new FirefoxDriver();
    }
    
    public static void close() {
        if (browser == null) {
            throw new IllegalStateException("Browser not initialized yet.");
        }
        browser.close();
    }
    
    public static WebDriver getBrowser() {
        return browser;
    }

}