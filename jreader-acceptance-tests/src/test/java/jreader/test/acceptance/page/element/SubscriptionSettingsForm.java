package jreader.test.acceptance.page.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SubscriptionSettingsForm {
    
    private WebDriver browser;
    
    @FindBy(css = "#subscription-settings .group-title .title")
    private List<WebElement> groupItems;
    
    @FindBy(css = "#subscription-settings .group-title .action.delete input")
    private List<WebElement> deleteGroupButtons;
    
    public SubscriptionSettingsForm(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }
    
    private static By getGroupTitleLocator(String title) {
        return By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title')]"
                + "//span[contains(@class, 'title') and .='" + title + "']");
    }
    
    public void editGroupTitle(String title) {
        browser.findElement(getGroupTitleLocator(title)).click();
    }
    
    public WebElement getGroupTitleFieldForEditing(String title) {
        return browser.findElement(By.xpath("//*[@id='subscription-settings']"
                + "//*[contains(@class, 'group-title') and //span[.='" + title + "']]"
                + "//form//input[@type='text']"));
    }
    
    public WebElement getSubscriptionTitleElement(String title, String groupTitle) {
        return browser.findElement(By.xpath("//*[@id='subscription-settings']"
                + "//*[contains(@class, 'settings-group') and //*[contains(@class, 'group-title')]//*[contains(@class, 'title') and .='" + groupTitle + "']]"
                + "//*[contains(@class, 'settings-item')]//span[.='" + title + "']"));
    }
    
    public List<WebElement> getGroupItems() {
        return groupItems;
    }
    
    public List<WebElement> getDeleteGroupButtons() {
        return deleteGroupButtons;
    }

}
