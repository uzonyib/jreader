package jreader.test.acceptance.step;

import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.Constants;
import jreader.test.acceptance.page.LoginPage;
import jreader.test.acceptance.page.Menu;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginLogoutStepsDef {
    
    private static final String PAGE_TITLE = "jReader";
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private LoginPage loginPage = new LoginPage(browser);
    private Menu menu = new Menu(browser);
    
    @When("^I open the main page URL$")
    public void i_open_the_main_page_url() throws Throwable {
        browser.get(Constants.MAIN_PAGE_URL);
    }
    
    @Then("^I should not see the main page$")
    public void i_should_not_see_the_main_page() throws Throwable {
        Assertions.assertThat(browser.getTitle()).isNotEqualTo(PAGE_TITLE);
    }
    
    @Given("^I am on the login page$")
    public void i_am_on_the_login_page() throws Throwable {
        loginPage.open();
    }
    
    @When("^I try to login as a non-admin user$")
    public void i_try_to_login_as_a_non_admin_user() throws Throwable {
        loginPage.login(false);
    }
    
    @When("^I try to login as an admin user$")
    public void i_try_to_login_as_an_admin_user() throws Throwable {
        loginPage.login(true);
    }
    
    @Then("^I should see the main page$")
    public void i_should_see_the_main_page() throws Throwable {
        Assertions.assertThat(browser.getTitle()).isEqualTo(PAGE_TITLE);
    }

    @Then("^I should see the menu$")
    public void i_should_see_the_menu() throws Throwable {
        Assertions.assertThat(menu.getLogoutMenuItem().isDisplayed()).isTrue();
        Assertions.assertThat(menu.getHomeMenuItem().isDisplayed()).isTrue();
        Assertions.assertThat(menu.getSettingsMenuItem().isDisplayed()).isTrue();
        Assertions.assertThat(menu.getAllItemsMenuItem().isDisplayed()).isTrue();
    }
    
    @Given("^I am logged in as an admin user$")
    public void i_am_logged_in_as_an_admin_user() throws Throwable {
        loginPage.login(true);
    }

    @When("^I click on the logout menu item$")
    public void i_click_on_the_logout_menu_item() throws Throwable {
        menu.logout();
    }

}
