package jreader.test.acceptance.page;

import java.util.List;

import jreader.test.acceptance.Constants;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Menu {
    
    private WebDriver browser;
    
    @FindBy(id = "logout-menu-item")
    @CacheLookup
    private WebElement logoutMenuItem;
    
    @FindBy(css = "#logout-menu-item a")
    @CacheLookup
    private WebElement logoutLink;
    
    @FindBy(id = "home-menu-item")
    @CacheLookup
    private WebElement homeMenuItem;
    
    @FindBy(id = "settings-menu-item")
    @CacheLookup
    private WebElement settingsMenuItem;
    
    @FindBy(id = "all-items-menu-item")
    @CacheLookup
    private WebElement allItemsMenuItem;
    
    @FindBy(css = "#subscription-menu .group-item .title")
    private List<WebElement> groupMenuItems;
    
    public Menu(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }
    
    public void logout() {
        logoutLink.click();
    }
    
    public void openHomePage(HomePage homePage) {
        homeMenuItem.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
            .until(ExpectedConditions.visibilityOf(homePage.getMainContent()));
    }
    
    public void openSettingsPage(SettingsPage settingsPage) {
        settingsMenuItem.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
            .until(ExpectedConditions.visibilityOf(settingsPage.getMainContent()));
    }

    public WebElement getLogoutMenuItem() {
        return logoutMenuItem;
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

}
