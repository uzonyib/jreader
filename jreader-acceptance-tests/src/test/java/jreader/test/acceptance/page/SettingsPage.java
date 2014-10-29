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

public class SettingsPage {
    
    private WebDriver browser;
    
    @FindBy(id = "settings-contents")
    @CacheLookup
    private WebElement mainContent;
    
    @FindBy(css = "#group-form input[name='title']")
    @CacheLookup
    private WebElement groupTitleField;
    
    @FindBy(css = "#group-form input[type='image']")
    @CacheLookup
    private WebElement createGroupButton;
    
    @FindBy(css = "#subscription-settings .group-title .title")
    private List<WebElement> groupItems;
    
    @FindBy(css = "#subscription-settings .group-title .action.delete input")
    private List<WebElement> deleteGroupButtons;
    
    public SettingsPage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }
    
    public void enterGroupTitle(String title) {
        groupTitleField.clear();
        groupTitleField.sendKeys(title);
    }
    
    public void clickCreateGroup() {
        createGroupButton.click();
    }
    
    public void createGroup(String title) {
        enterGroupTitle(title);
        clickCreateGroup();
        waitForGroupToBeCreated(title);
    }
    
    public void createGroups(List<String> titles) {
        for (String title : titles) {
            createGroup(title);
        }
    }
    
    private static By getGroupTitleLocator(String title) {
        return By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title')]"
                + "//span[contains(@class, 'title') and .='" + title + "']");
    }
    
    public void waitForGroupToBeCreated(String title) {
        new WebDriverWait(browser, 5).until(ExpectedConditions.presenceOfElementLocated(getGroupTitleLocator(title)));
    }
    
    public void editGroupTitle(String title) {
        browser.findElement(getGroupTitleLocator(title)).click();
    }
    
    public WebElement getGroupTitleFieldForEditing(String title) {
        return browser.findElement(By.xpath("//*[@id='subscription-settings']"
                + "//*[contains(@class, 'group-title') and //span[.='" + title + "']]"
                + "//form//input[@type='text']"));
    }
    
    public WebElement getMainContent() {
        return mainContent;
    }
    
    public WebElement getGroupTitleField() {
        return groupTitleField;
    }
    
    public WebElement getCreateGroupButton() {
        return createGroupButton;
    }
    
    public List<WebElement> getGroupItems() {
        return groupItems;
    }
    
    public List<WebElement> getDeleteGroupButtons() {
        return deleteGroupButtons;
    }

}
