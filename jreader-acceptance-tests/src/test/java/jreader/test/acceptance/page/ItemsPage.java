package jreader.test.acceptance.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jreader.test.acceptance.util.Constants;

@Component
public class ItemsPage {

    private WebDriver browser;

    @FindBy(id = "posts-contents")
    private WebElement mainContent;

    @FindBy(xpath = "//button[@title = 'Mark all as read (M)']")
    private WebElement markAllPostsReadButton;

    @FindBy(xpath = "//button[@title = 'Refresh (R)']")
    private WebElement refreshButton;

    @FindBy(xpath = "//*[contains(@class, 'btn') and @title = 'All (1)']")
    private WebElement allPostsButton;

    @FindBy(xpath = "//*[contains(@class, 'btn') and @title = 'Unread (2)']")
    private WebElement unreadPostsButton;

    @FindBy(xpath = "//*[contains(@class, 'btn') and @title = 'Bookmarked (3)']")
    private WebElement bookmarkedPostsButton;

    @FindBy(xpath = "//*[contains(@class, 'btn') and @title = 'Descending (D)']")
    private WebElement descendingOrderButton;

    @Autowired
    public ItemsPage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    public WebElement getMainContent() {
        return mainContent;
    }

    public boolean isDisplayed() {
        return mainContent != null && mainContent.isDisplayed();
    }

    public String getGroupOfPostAt(int index) {
        return browser.findElement(By.xpath("//*[contains(@class, 'article-breadcrumb')][" + (index + 1) + "]/*[contains(@class, 'feed-title')]")).getText();
    }

    public String getTitleOfPostAt(int index) {
        return browser.findElement(By.xpath(
                "//*[contains(@class, 'article-breadcrumb')][" + (index + 1) + "]" + "/*[not(contains(@class, 'feed-title')) and contains(@class, 'title')]"))
                .getText();
    }

    public void waitForPostsToLoad() {
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[contains(@class, 'article-breadcrumb')]"), 0));
    }

    public void openPost(String title) {
        browser.findElement(By.xpath("//*[contains(@class, 'article-breadcrumb')]/*[contains(@class, 'title') and .='" + title + "']")).click();
    }

    public boolean isDescriptionDisplayed(String description) {
        return browser.findElement(By.xpath("//*[contains(@class, 'article-detail')]//*[contains(@class, 'description') and .='" + description + "']"))
                .isDisplayed();
    }

    public boolean isAuthorDisplayed(String author) {
        return browser.findElement(By.xpath("//*[contains(@class, 'article-detail')]//*[contains(text(), '" + author + "')]")).isDisplayed();
    }

    public boolean isLinkDisplayed(String url) {
        return browser.findElement(By.xpath("//*[contains(@class, 'article-detail')]//a[@href='" + url + "']")).isDisplayed();
    }

    public int getNumberOfPostsDisplayed() {
        return browser.findElements(By.xpath("//*[contains(@class, 'article-breadcrumb')]")).size();
    }

    public void markAllPostsRead() {
        markAllPostsReadButton.click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
                .until(ExpectedConditions.numberOfElementsToBe(By.xpath("//*[contains(@class, 'article-breadcrumb')]"), 0));
    }

    public void refresh() {
        refreshButton.click();
        waitForPostsToLoad();
    }

    public void openAllPosts() {
        allPostsButton.click();
        waitForPostsToLoad();
    }

    public void openUnreadPosts() {
        unreadPostsButton.click();
        waitForPostsToLoad();
    }

    public void openBookmarkedPosts() {
        bookmarkedPostsButton.click();
        waitForPostsToLoad();
    }

    public void bookmarkPost(String title) {
        browser.findElement(By.xpath("//*[contains(@class, 'article-breadcrumb') and .//*[contains(@class, 'title') and .='" + title
                + "']]//button[contains(@class, 'bookmark') and not(contains(@class, 'delete-bookmark'))]")).click();
    }

    public void deleteBookmarkOfPost(String title) {
        browser.findElement(By.xpath("//*[contains(@class, 'article-breadcrumb') and .//*[contains(@class, 'title') and .='" + title
                + "']]//button[contains(@class, 'delete-bookmark')]")).click();
    }

    public void orderPostsDescending() {
        descendingOrderButton.click();
        waitForPostsToLoad();
    }

    public void selectArchive(String title) {
        browser.findElement(By.xpath("//*[contains(@class, 'article-detail')]//*[contains(@class, 'dropdown')]/button")).click();
        browser.findElement(By.xpath("//*[contains(@class, 'article-detail')]//*[contains(@class, 'dropdown')]//a[.='" + title + "']")).click();
    }

    public void clickArchiveButton() {
        browser.findElement(By.xpath("//*[contains(@class, 'article-detail')]//button[@type='submit' and @title='Archive']")).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
                .until(ExpectedConditions.numberOfElementsToBe(By.xpath("//*[contains(@class, 'article-detail')]//*[contains(text(), 'Archived to')]"), 1));
    }

    public boolean isArchiveMessageVisible(String message) {
        return browser.findElement(By.xpath("//*[contains(@class, 'article-detail')]//*[contains(text(), '" + message + "')]")).isDisplayed();
    }

    public void archive(String post, String archive) {
        openPost(post);
        selectArchive(archive);
        clickArchiveButton();
    }

}
