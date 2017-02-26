package jreader.test.acceptance.page.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jreader.test.acceptance.page.ArchivesPage;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.ItemsPage;
import jreader.test.acceptance.page.SettingsPage;
import jreader.test.acceptance.util.Constants;

@Component
public class Menu {

    private WebDriver browser;

    @Autowired
    private HomePage homePage;
    @Autowired
    private SettingsPage settingsPage;
    @Autowired
    private ItemsPage itemsPage;
    @Autowired
    private ArchivesPage archivesPage;

    @FindBy(id = "menu")
    private WebElement mainContent;

    @FindBy(id = "logout-button")
    private WebElement logoutButton;

    @FindBy(id = "home-menu-item")
    private WebElement homeMenuItem;

    @FindBy(id = "settings-menu-item")
    private WebElement settingsMenuItem;

    @FindBy(id = "all-items-menu-item")
    private WebElement allItemsMenuItem;

    @FindBy(css = "#menu .group-item .title")
    private List<WebElement> groupMenuItems;

    @FindBy(id = "archives-menu-item")
    private WebElement archivesMenuItem;

    @FindBy(xpath = "//*[@id='archives-menu-item']/span[1]")
    private WebElement archivesMenuItemExpander;

    @FindBy(css = "#menu .archive-item .title")
    private List<WebElement> archiveMenuItems;

    @FindBy(css = "#all-items-menu-item .badge")
    private WebElement allItemsUnreadCount;

    @Autowired
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
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.visibilityOf(homePage.getMainContent()));
    }

    public void openSettingsPage() {
        settingsMenuItem.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.visibilityOf(settingsPage.getMainContent()));
    }

    public void openAllItemsPage() {
        allItemsMenuItem.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.visibilityOf(itemsPage.getMainContent()));
    }

    public void openArchives() {
        archivesMenuItem.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.visibilityOf(archivesPage.getMainContent()));
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

    public String getAllItemsUnreadCount() {
        return allItemsUnreadCount.getText();
    }

    public String getGroupUnreadCount(String title) {
        return browser.findElement(By.xpath("//*[@id='menu']//*[contains(@class, 'group-item') and .//span[contains(@class, 'title') and .='" + title
                + "']]//*[contains(@class, 'count')]")).getText();
    }

    public String getSubscriptionUnreadCount(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='menu']//*[contains(@class, 'feed-item') and .//span[contains(@class, 'title') and .='" + title + "']]//*[contains(@class, 'count')]"))
                .getText();
    }

    public void expandGroup(String title) {
        browser.findElement(By.xpath("//*[@id='menu']//*[contains(@class, 'group-item') and .//span[contains(@class, 'title') and .='" + title + "']]/span[1]"))
                .click();
    }

    public void openGroup(String title) {
        browser.findElement(By.xpath("//*[@id='menu']//*[contains(@class, 'group-item')]//span[contains(@class, 'title') and .='" + title + "']")).click();
    }

    public void openSubscription(String title) {
        getSubscriptionMenuItem(title).click();
    }

    public WebElement getSubscriptionMenuItem(String title) {
        return browser.findElement(By.xpath("//*[@id='menu']//*[contains(@class, 'feed-item') and .//span[contains(@class, 'title') and .='" + title + "']]"));
    }

    public List<WebElement> getArchiveMenuItems() {
        return archiveMenuItems;
    }

    public void expandArchives() {
        archivesMenuItemExpander.click();
    }

    public void openArchive(String title) {
        browser.findElement(By.xpath("//*[@id='menu']//*[contains(@class, 'archive-item')]//span[contains(@class, 'title') and .='" + title + "']")).click();
    }

}
