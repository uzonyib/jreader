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

import jreader.test.acceptance.util.Constants;

@Component
public class ArchiveSettingsForm {

    private WebDriver browser;

    @FindBy(css = "#archive-settings .settings-item span.title")
    private List<WebElement> archiveItems;

    @Autowired
    public ArchiveSettingsForm(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    private static By getArchiveTitleLocator(String title) {
        return By.xpath("//*[@id='archive-settings']//*[contains(@class, 'settings-item')]//span[contains(@class, 'title') and .='" + title + "']");
    }

    public void editArchiveTitle(String title) {
        browser.findElement(getArchiveTitleLocator(title)).click();
    }

    public WebElement getArchiveTitleFieldForEditing(String title) {
        return browser.findElement(
                By.xpath("//*[@id='archive-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]//form//input[@type='text']"));
    }

    private WebElement getMoveDownButtonOfArchive(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='archive-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]" + "//form//button[@title='Move down']"));
    }

    private WebElement getMoveUpButtonOfArchive(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='archive-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]" + "//form//button[@title='Move up']"));
    }

    private WebElement getRenameButtonOfArchive(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='archive-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]" + "//form//button[@title='Update']"));
    }

    private WebElement getDeleteButtonOfArchive(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='archive-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]" + "//form//button[@title='Delete']"));
    }

    private By getArchiveWithTitleAt(String title, int expectedIndex) {
        return By.xpath("(//*[@id='archive-settings']//*[contains(@class, 'settings-item')])[" + (expectedIndex + 1) + "]" + "//span[.='" + title + "']");
    }

    private int indexOf(String title) {
        for (int i = 0; i < archiveItems.size(); ++i) {
            if (title.equals(archiveItems.get(i).getText())) {
                return i;
            }
        }
        throw new RuntimeException("Archive \"" + title + "\" not found");
    }

    public List<WebElement> getArchiveItems() {
        return archiveItems;
    }

    public void waitForArchiveToBeCreated(String title) {
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getArchiveTitleLocator(title)));
    }

    public void moveArchiveDown(String title) {
        int expectedIndex = indexOf(title) + 1;
        getMoveDownButtonOfArchive(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getArchiveWithTitleAt(title, expectedIndex)));
    }

    public void moveArchiveUp(String title) {
        int expectedIndex = indexOf(title) - 1;
        getMoveUpButtonOfArchive(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getArchiveWithTitleAt(title, expectedIndex)));
    }

    public void renameArchive(String from, String to) {
        int expectedIndex = indexOf(from);
        editArchiveTitle(from);
        WebElement titleField = getArchiveTitleFieldForEditing(from);
        titleField.clear();
        titleField.sendKeys(to);
        getRenameButtonOfArchive(from).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getArchiveWithTitleAt(to, expectedIndex)));
    }

    public void deleteArchive(String title) {
        int expectedCount = archiveItems.size() - 1;
        getDeleteButtonOfArchive(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(
                ExpectedConditions.numberOfElementsToBe(By.xpath("//*[@id='archive-settings']//*[contains(@class, 'settings-item')]"), expectedCount));
    }

}
