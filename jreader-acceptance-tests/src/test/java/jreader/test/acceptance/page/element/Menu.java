package jreader.test.acceptance.page.element;

import java.util.List;

import jreader.test.acceptance.Constants;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.SettingsPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Menu {
    
    private WebDriver browser;
    
    private HomePage homePage;
    private SettingsPage settingsPage;
    
    @FindBy(id = "menu")
    @CacheLookup
    private WebElement mainContent;
    
    @FindBy(id = "logout-button")
    @CacheLookup
    private WebElement logoutButton;
    
    @FindBy(id = "home-menu-item")
    @CacheLookup
    private WebElement homeMenuItem;
    
    @FindBy(id = "settings-menu-item")
    @CacheLookup
    private WebElement settingsMenuItem;
    
    @FindBy(id = "all-items-menu-item")
    @CacheLookup
    private WebElement allItemsMenuItem;
    
    @FindBy(css = "#menu .group-item .title")
    private List<WebElement> groupMenuItems;
    
    public Menu(WebDriver browser) {
        this.browser = browser;
        this.homePage = new HomePage(browser);
        settingsPage = new SettingsPage(browser);
        PageFactory.initElements(browser, this);
    }
    
    public boolean isDisplayed() {
        return mainContent != null && mainContent.isDisplayed();
    }
    
    public void logout() {
        logoutButton.click();
    }
    
    public void openHomePage() {
        homeMenuItem.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
            .until(ExpectedConditions.visibilityOf(homePage.getMainContent()));
    }
    
    public void openSettingsPage() {
        settingsMenuItem.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
            .until(ExpectedConditions.visibilityOf(settingsPage.getMainContent()));
    }

    public WebElement getLogoutButton() {
        return logoutButton;
    }

    public WebElement getHomeMenuItem() {
        return homeMenuItem;
    }

    public WebElement getSettingsMenuItem() {
        return settingsMenuItem;
    }

    public WebElement getAllItemsMenuItem() {
        return allItemsMenuItem;
    }
    
    public List<WebElement> getGroupMenuItems() {
        return groupMenuItems;
    }
    
    public WebElement getGroupUnreadCount(String title) {
        return browser.findElement(By.xpath("//*[@id='menu']"
                + "//*[contains(@class, 'group-item') and //span[contains(@class, 'title') and .='" + title + "']]"
                + "//*[contains(@class, 'unread-count')]"));
    }

}
