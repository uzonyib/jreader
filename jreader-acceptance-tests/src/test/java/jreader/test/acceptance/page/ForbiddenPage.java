package jreader.test.acceptance.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class ForbiddenPage {

    private static final String TITLE = "jReader - Not authorized";

    private WebDriver browser;

    public ForbiddenPage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    public boolean isDisplayed() {
        return TITLE.equals(browser.getTitle());
    }

}
