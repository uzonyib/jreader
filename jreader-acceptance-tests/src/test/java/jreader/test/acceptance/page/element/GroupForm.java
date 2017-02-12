package jreader.test.acceptance.page.element;

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
public class GroupForm {

    private WebDriver browser;

    @FindBy(css = "#group-form input[name='title']")
    private WebElement titleField;

    @FindBy(css = "#group-form button[type='submit']")
    private WebElement createButton;

    @Autowired
    public GroupForm(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    public void enterTitle(String title) {
        titleField.clear();
        titleField.sendKeys(title);
    }

    public void clickCreateButton() {
        createButton.click();
    }

    public void createGroup(String title) {
        enterTitle(title);
        clickCreateButton();
        waitForGroupToBeCreated(title);
    }

    public void createGroups(String[] titles) {
        for (String title : titles) {
            createGroup(title);
        }
    }

    private static By getGroupTitleLocator(String title) {
        return By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'group-title')]" + "//span[contains(@class, 'title') and .='" + title + "']");
    }

    public void waitForGroupToBeCreated(String title) {
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(getGroupTitleLocator(title)));
    }

    public WebElement getTitleField() {
        return titleField;
    }

    public WebElement getCreateButton() {
        return createButton;
    }

}
