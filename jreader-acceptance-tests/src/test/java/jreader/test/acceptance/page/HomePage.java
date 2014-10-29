package jreader.test.acceptance.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    
    private WebDriver browser;
    
    @FindBy(id = "home-contents")
    @CacheLookup
    private WebElement mainContent;
    
    @FindBy(css = "#subscription-group-stats .group-title")
    private List<WebElement> groupItems;
    
    public HomePage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }
    
    public WebElement getMainContent() {
        return mainContent;
    }
    
    public List<WebElement> getGroupItems() {
        return groupItems;
    }

}
