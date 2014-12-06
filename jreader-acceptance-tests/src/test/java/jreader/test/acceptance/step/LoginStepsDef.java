package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.Constants;
import jreader.test.acceptance.page.LoginPage;

import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginStepsDef {
    
    private static final String PAGE_TITLE = "jReader";
    private static final String ERROR_TITLE_PREFIX = "Error 403";
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private LoginPage loginPage = new LoginPage(browser);
    
    private boolean isAdmin = true;
    
    @When("^I open the main page URL$")
    public void i_open_the_main_page_url() {
        browser.get(Constants.MAIN_PAGE_URL);
    }
    
    @Then("^the login page should be displayed$")
    public void the_login_page_should_be_displayed() {
        assertThat(loginPage.isDisplayed()).isTrue();
    }
    
    @Given("^I am not an admin$")
    public void i_am_not_an_admin() {
        isAdmin = false;
    }
    
    @Given("^I am on the login page$")
    public void i_am_on_the_login_page() {
        loginPage.open();
    }
    
    @When("^I try to login$")
    public void i_try_to_login() {
        loginPage.login(isAdmin);
    }
    
    @Then("^the error page should be displayed$")
    public void the_error_page_should_be_displayed() {
        assertThat(browser.getTitle()).startsWith(ERROR_TITLE_PREFIX);
    }
    
    @Given("^I am an admin$")
    public void i_am_an_admin() {
        isAdmin = true;
    }
    
    @Then("^the main page should be displayed$")
    public void the_main_page_should_be_displayed() {
        assertThat(browser.getTitle()).isEqualTo(PAGE_TITLE);
    }
    
    @Given("^I am logged in$")
    public void i_am_logged_in() {
        loginPage.login(true);
    }
    
    @When("^I log in$")
    public void i_log_in() {
        loginPage.login(true);
    }

}
