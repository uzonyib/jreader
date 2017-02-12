package jreader.test.acceptance.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jreader.test.acceptance.page.element.UserRoleTable;
import jreader.test.acceptance.util.Constants;

@Component
public class UserAdminPage {

    private static final String TITLE = "jReader - Users";

    private WebDriver browser;

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "isAdmin")
    private WebElement adminCheckbox;

    @FindBy(id = "btn-login")
    private WebElement loginButton;

    private UserRoleTable userRoleTable;

    @Autowired
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
