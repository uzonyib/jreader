package jreader.test.acceptance.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomePage {

    private WebDriver browser;

    @FindBy(id = "home-contents")
    private WebElement mainContent;

    @FindBy(css = "#subscription-group-stats .group-title")
    private List<WebElement> groupItems;

    @Autowired
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

    public boolean isSubscriptionDisplayed(String title) {
        return browser.findElement(By.xpath("(//*[contains(@class, 'subscription-stat')]/span[contains(@class, 'title') and .='" + title + "'])"))
                .isDisplayed();
    }

}
