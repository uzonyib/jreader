package jreader.test.acceptance.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ForbiddenPage {

    private static final String TITLE = "jReader - Not authorized";

    private WebDriver browser;

    @Autowired
    public ForbiddenPage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    public boolean isDisplayed() {
        return TITLE.equals(browser.getTitle());
    }

}
