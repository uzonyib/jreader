package jreader.test.acceptance.page;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    
    private WebDriver browser;
    
    @FindBy(id = "home-contents")
    @CacheLookup
    private WebElement mainContent;
    
    @FindBy(css = "#subscription-group-stats .group-title")
    private List<WebElement> groupItems;
    
    public HomePage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(this.browser, this);
    }
    
    public WebElement getMainContent() {
        return mainContent;
    }
    
    public List<WebElement> getGroupItems() {
        return groupItems;
    }
    
    public boolean isDisplayed() {
        return mainContent != null && mainContent.isDisplayed();
    }

}
