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
public class ArchivesPage {

    private WebDriver browser;

    @FindBy(id = "archives-contents")
    private WebElement mainContent;

    @FindBy(xpath = "//button[@title = 'Refresh (R)']")
    private WebElement refreshButton;

    @FindBy(xpath = "//*[contains(@class, 'btn') and @title = 'Descending (D)']")
    private WebElement descendingOrderButton;

    @Autowired
    public ArchivesPage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    public WebElement getMainContent() {
        return mainContent;
    }

    public boolean isDisplayed() {
        return mainContent != null && mainContent.isDisplayed();
    }

    public String getTitleOfPostAt(int index) {
        return browser
                .findElement(
                        By.xpath("//*[@id='archives-contents']//*[contains(@class, 'article-breadcrumb')][" + (index + 1) + "]/*[contains(@class, 'title')]"))
                .getText();
    }

    public void waitForPostsToLoad() {
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//*[contains(@class, 'article-breadcrumb')]"), 0));
    }

    public void openPost(String title) {
        browser.findElement(getPostTitleSelector(title)).click();
    }

    private static By getPostTitleSelector(String title) {
        return By.xpath("//*[@id='archives-contents']//*[contains(@class, 'article-breadcrumb')]/*[contains(@class, 'title') and .='" + title + "']");
    }

    public boolean isDescriptionDisplayed(String description) {
        return browser
                .findElement(By.xpath(
                        "//*[@id='archives-contents']//*[contains(@class, 'article-detail')]//*[contains(@class, 'description') and .='" + description + "']"))
                .isDisplayed();
    }

    public boolean isAuthorDisplayed(String author) {
        return browser.findElement(By.xpath("//*[@id='archives-contents']//*[contains(@class, 'article-detail')]//*[contains(text(), '" + author + "')]"))
                .isDisplayed();
    }

    public boolean isLinkDisplayed(String url) {
        return browser.findElement(By.xpath("//*[@id='archives-contents']//*[contains(@class, 'article-detail')]//a[@href='" + url + "']")).isDisplayed();
    }

    public void deletePost(String title) {
        browser.findElement(By.xpath("//*[@id='archives-contents']//*[contains(@class, 'article-breadcrumb') and .//*[contains(@class, 'title') and .='" + title
                + "']]//button[contains(@class, 'delete')]")).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.numberOfElementsToBe(getPostTitleSelector(title), 0));
    }

}
