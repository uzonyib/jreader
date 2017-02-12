package jreader.test.acceptance.page.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;

import jreader.test.acceptance.util.Constants;

@Component
public class SubscriptionForm {

    private WebDriver browser;

    @FindBy(css = "#subscription-form .dropdown ul li a")
    private List<WebElement> groupOptions;

    @FindBy(css = "#subscription-form .dropdown button span.title")
    private WebElement selectedGroupOption;

    @FindBy(css = "#subscription-form .dropdown button")
    private WebElement groupDropdown;

    @FindBy(css = "#subscription-form input[type='text']")
    private WebElement urlField;

    @FindBy(css = "#subscription-form button[type='submit']")
    private WebElement subscribeButton;

    @Autowired
    public SubscriptionForm(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    public String getSelectedGroupTitle() {
        return selectedGroupOption.getText();
    }

    public void openGroupDropdown() {
        groupDropdown.click();
    }

    public void selectGroupTitle(String title) {
        openGroupDropdown();
        for (WebElement groupOption : groupOptions) {
            if (groupOption.getText().equals(title)) {
                groupOption.click();
                break;
            }
        }
    }

    public void enterUrl(String url) {
        urlField.clear();
        urlField.sendKeys(url);
    }

    public void clickSubscribeButton() {
        subscribeButton.click();
    }

    private static By getSubscriptionItemsLocator() {
        return By.xpath("//*[@id='subscription-settings']//*[contains(@class, 'settings-item')]");
    }

    public void waitForSubscriptionToBeCreated() {
        final int itemCount = browser.findElements(getSubscriptionItemsLocator()).size();
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT).until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver browser) {
                return itemCount < browser.findElements(getSubscriptionItemsLocator()).size();
            }
        });
    }

    public List<WebElement> getGroupOptions() {
        return groupOptions;
    }

    public WebElement getGroupDropdown() {
        return groupDropdown;
    }

    public WebElement getUrlField() {
        return urlField;
    }

    public WebElement getSubscribeButton() {
        return subscribeButton;
    }

}
