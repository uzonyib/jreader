package jreader.test.acceptance.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import jreader.test.acceptance.Constants;
import jreader.test.acceptance.page.element.UserRoleTable;

public class UserAdminPage {
    
    private static final String TITLE = "jReader - Users";
    
    private WebDriver browser;
    
    @FindBy(id = "email")
    private WebElement emailField;
    
    @FindBy(id = "isAdmin")
    @CacheLookup
    private WebElement adminCheckbox;
    
    @FindBy(id = "btn-login")
    @CacheLookup
    private WebElement loginButton;
    
    private UserRoleTable userRoleTable;
    
    public UserAdminPage(WebDriver browser) {
        this.browser = browser;
        PageFactory.initElements(browser, this);
        this.userRoleTable = new UserRoleTable(browser);
    }
    
    public UserRoleTable getUserRoleTable() {
        return userRoleTable;
    }
    
    public void open() {
        browser.get(Constants.USER_ADMIN_PAGE_URL);
    }
    
    public boolean isDisplayed() {
        return TITLE.equals(browser.getTitle());
    }

}
