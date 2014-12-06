package jreader.test.acceptance.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ItemsPage {
    
    @FindBy(id = "items-contents")
    @CacheLookup
    private WebElement mainContent;
    
    public ItemsPage(WebDriver browser) {
        PageFactory.initElements(browser, this);
    }
    
    public WebElement getMainContent() {
        return mainContent;
    }
    
    public boolean isDisplayed() {
        return mainContent != null && mainContent.isDisplayed();
    }

}
