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
public class SubscriptionSettingsForm {

    private WebDriver browser;

    @FindBy(css = "#subscription-settings .group-title .title")
    private List<WebElement> groupItems;

    @Autowired
    public SubscriptionSettingsForm(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    private static By getGroupTitleLocator(String title) {
        return By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title')]//span[contains(@class, 'title') and .='" + title + "']");
    }

    public void waitForGroupToBeCreated(String title) {
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getGroupTitleLocator(title)));
    }

    public void editGroupTitle(String title) {
        browser.findElement(getGroupTitleLocator(title)).click();
    }

    public WebElement getGroupTitleFieldForEditing(String title) {
        return browser.findElement(
                By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title') and .//span[.='" + title + "']]//form//input[@type='text']"));
    }

    public WebElement getSubscriptionTitleElement(String title, String groupTitle) {
        return browser.findElement(By.xpath("//*[@id='subscription-settings']"
                + "//*[contains(@class, 'settings-group') and .//*[contains(@class, 'group-title')]//*[contains(@class, 'title') and .='" + groupTitle + "']]"
                + "//*[contains(@class, 'settings-item')]//span[.='" + title + "']"));
    }

    private WebElement getMoveDownButtonOfGroup(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='subscription-settings']//*[contains(@class, 'group-title') and .//span[.='" + title + "']]//form//button[@title='Move down']"));
    }

    private WebElement getMoveUpButtonOfGroup(String title) {
        return browser.findElement(By
                .xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title') and .//span[.='" + title + "']]//form//button[@title='Move up']"));
    }

    private WebElement getRenameButtonOfGroup(String title) {
        return browser.findElement(
                By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title') and .//span[.='" + title + "']]//form//button[@title='Update']"));
    }

    private WebElement getDeleteButtonOfGroup(String title) {
        return browser.findElement(
                By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title') and .//span[.='" + title + "']]//form//button[@title='Delete']"));
    }

    private By getGroupWithTitleAt(String title, int expectedIndex) {
        return By.xpath("(//*[@id='subscription-settings']//*[contains(@class, 'group-title')])[" + (expectedIndex + 1) + "]//span[.='" + title + "']");
    }

    private int groupIndexOf(String title) {
        for (int i = 0; i < groupItems.size(); ++i) {
            if (title.equals(groupItems.get(i).getText())) {
                return i;
            }
        }
        throw new RuntimeException("Group \"" + title + "\" not found");
    }

    public List<WebElement> getGroupItems() {
        return groupItems;
    }

    public void moveGroupDown(String title) {
        int expectedIndex = groupIndexOf(title) + 1;
        getMoveDownButtonOfGroup(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getGroupWithTitleAt(title, expectedIndex)));
    }

    public void moveGroupUp(String title) {
        int expectedIndex = groupIndexOf(title) - 1;
        getMoveUpButtonOfGroup(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getGroupWithTitleAt(title, expectedIndex)));
    }

    public void renameGroup(String from, String to) {
        int expectedIndex = groupIndexOf(from);
        editGroupTitle(from);
        WebElement titleField = getGroupTitleFieldForEditing(from);
        titleField.clear();
        titleField.sendKeys(to);
        getRenameButtonOfGroup(from).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getGroupWithTitleAt(to, expectedIndex)));
    }

    public void deleteGroup(String title) {
        int expectedCount = groupItems.size() - 1;
        getDeleteButtonOfGroup(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(
                ExpectedConditions.numberOfElementsToBe(By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title')]"), expectedCount));
    }

    private List<WebElement> getSubscriptionItems(String groupTitle) {
        return browser
                .findElements(By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'settings-group') and .//span[contains(@class, 'title') and .='"
                        + groupTitle + "']]//*[contains(@class, 'settings-item')]/*[contains(@class, 'title')]/span"));
    }

    private int subscriptionIndexOf(String groupTitle, String title) {
        final List<WebElement> subscriptionItems = getSubscriptionItems(groupTitle);
        for (int i = 0; i < subscriptionItems.size(); ++i) {
            if (title.equals(subscriptionItems.get(i).getText())) {
                return i;
            }
        }
        throw new RuntimeException("Subscription \"" + title + "\" not found");
    }

    private By getSubscriptionWithTitleAt(String groupTitle, String title, int expectedIndex) {
        return By.xpath("(//*[@id='subscription-settings']//*[contains(@class, 'settings-group') and .//span[contains(@class, 'title') and .='" + groupTitle
                + "']]//*[contains(@class, 'settings-item')])[" + (expectedIndex + 1) + "]//span[.='" + title + "']");
    }

    private WebElement getMoveDownButtonOfSubscription(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='subscription-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]//form//button[@title='Move down']"));
    }

    private WebElement getMoveUpButtonOfSubscription(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='subscription-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]//form//button[@title='Move up']"));
    }

    public void moveSubscriptionDown(String groupTitle, String title) {
        int expectedIndex = subscriptionIndexOf(groupTitle, title) + 1;
        getMoveDownButtonOfSubscription(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(getSubscriptionWithTitleAt(groupTitle, title, expectedIndex)));
    }

    public void moveSubscriptionUp(String groupTitle, String title) {
        int expectedIndex = subscriptionIndexOf(groupTitle, title) - 1;
        getMoveUpButtonOfSubscription(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(getSubscriptionWithTitleAt(groupTitle, title, expectedIndex)));
    }

    private static By getSubscriptionTitleLocator(String title) {
        return By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'settings-item')]//span[.='" + title + "']");
    }

    public void editSubscriptionTitle(String title) {
        browser.findElement(getSubscriptionTitleLocator(title)).click();
    }

    public WebElement getSubscriptionTitleFieldForEditing(String title) {
        return browser.findElement(
                By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]//form//input[@type='text']"));
    }

    private WebElement getRenameButtonOfSubscription(String title) {
        return browser.findElement(By
                .xpath("//*[@id='subscription-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]//form//button[@title='Update']"));
    }

    public void renameSubscription(String groupTitle, String from, String to) {
        int expectedIndex = subscriptionIndexOf(groupTitle, from);
        editSubscriptionTitle(from);
        WebElement titleField = getSubscriptionTitleFieldForEditing(from);
        titleField.clear();
        titleField.sendKeys(to);
        getRenameButtonOfSubscription(from).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(getSubscriptionWithTitleAt(groupTitle, to, expectedIndex)));
    }

    private WebElement getDeleteButtonOfSubscription(String title) {
        return browser.findElement(By.xpath(
                "//*[@id='subscription-settings']//*[contains(@class, 'settings-item') and .//span[.='" + title + "']]//form//button[@title='Unsubscribe']"));
    }

    public void deleteSubscription(String groupTitle, String title) {
        int expectedCount = getSubscriptionItems(groupTitle).size() - 1;
        getDeleteButtonOfSubscription(title).click();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(
                ExpectedConditions.numberOfElementsToBe(By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'settings-item')]"), expectedCount));
    }

}
