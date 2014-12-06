package jreader.test.acceptance.page.element;

import jreader.test.acceptance.Constants;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

public class SubscriptionForm {
    
    private WebDriver browser;
    
    @FindBy(css = "#subscription-form select")
    @CacheLookup
    private WebElement groupField;
    
    @FindBy(css = "#subscription-form input[type='text']")
    @CacheLookup
    private WebElement urlField;
    
    @FindBy(css = "#subscription-form input[type='image']")
    @CacheLookup
    private WebElement subscribeButton;
    
    public SubscriptionForm(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }
    
    public String getSelectedGroupTitle() {
        return getGroupField().getFirstSelectedOption().getText();
    }
    
    public void selectGroupTitle(String title) {
        getGroupField().selectByVisibleText(title);
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
        new WebDriverWait(browser, Constants.WAIT_TIMEOUT)
                .until(new Predicate<WebDriver>() {
                    @Override
                    public boolean apply(WebDriver browser) {
                        return itemCount < browser.findElements(getSubscriptionItemsLocator()).size();
                    }
                });
    }
    
    public Select getGroupField() {
        return new Select(groupField);
    }
    
    public WebElement getUrlField() {
        return urlField;
    }
    
    public WebElement getSubscribeButton() {
        return subscribeButton;
    }
    
}
