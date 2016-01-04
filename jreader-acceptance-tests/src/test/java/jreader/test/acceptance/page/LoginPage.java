package jreader.test.acceptance.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import jreader.test.acceptance.Constants;

public class LoginPage {
    
    private WebDriver browser;
    
    @FindBy(id = "email")
    private WebElement emailField;
    
    @FindBy(id = "isAdmin")
    @CacheLookup
    private WebElement adminCheckbox;
    
    @FindBy(id = "btn-login")
    @CacheLookup
    private WebElement loginButton;
    
    public LoginPage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }
    
    public void open() {
        browser.get(Constants.LOGIN_PAGE_URL);
    }
    
    public void enterEmail(String email) {
        emailField.clear();
        emailField.sendKeys(email);
    }
    
    public void checkAdmin(boolean isAdmin) {
        if (isAdmin != adminCheckbox.isSelected()) {
            adminCheckbox.click();
        }
    }
    
    public void clickLogin() {
        loginButton.click();
    }
    
    public void login(String email, boolean isAdmin) {
        open();
        enterEmail(email);
        checkAdmin(isAdmin);
        clickLogin();
    }
    
    public boolean isDisplayed() {
        return emailField != null && emailField.isDisplayed();
    }

}
