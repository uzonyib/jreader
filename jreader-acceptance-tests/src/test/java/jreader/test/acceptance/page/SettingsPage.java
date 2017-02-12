package jreader.test.acceptance.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SettingsPage {

    @FindBy(id = "settings-contents")
    private WebElement mainContent;

    @Autowired
    public SettingsPage(WebDriver browser) {
        PageFactory.initElements(browser, this);
    }

    public WebElement getMainContent() {
        return mainContent;
    }

    public boolean isDisplayed() {
        return mainContent != null && mainContent.isDisplayed();
    }

}
