package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.UUID;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.page.ForbiddenPage;
import jreader.test.acceptance.page.LoginPage;
import jreader.test.acceptance.util.Constants;

public class LoginStepsDef extends StepDefs {

    @Autowired
    private WebDriver browser;

    @Autowired
    private LoginPage loginPage;
    @Autowired
    private ForbiddenPage forbiddenPage;

    private String username;
    private boolean isAdmin = true;

    @Given("^\"(.*?)\" is an administrator$")
    public void setAdmin(String username) {
        this.username = username;
        this.isAdmin = true;
    }

    @Given("^\"(.*?)\" is not an administrator$")
    public void setNonAdmin(String username) {
        this.username = username;
        this.isAdmin = false;
    }

    @Given("^he is on the login page$")
    public void openLoginPage() {
        loginPage.open();
    }

    @Given("^a new user is logged in$")
    public void ensureLoggedInAsAdmin() {
        resetUser();
        generateUsername();
        this.isAdmin = true;
        loginPage.login(username, true);
    }

    @Given("^a new user$")
    public void ensureNewUser() {
        resetUser();
        generateUsername();
        this.isAdmin = true;
    }

    @Given("^he is logged in$")
    public void ensureLoggedIn() {
        loginPage.login(username, isAdmin);
    }

    @When("^he opens the main page$")
    public void openMainPage() throws IOException {
        browser.get(Constants.MAIN_PAGE_URL);
    }

    @Then("^the login page is displayed$")
    public void checkLoginPageIsDisplayed() {
        assertThat(loginPage.isDisplayed()).isTrue();
    }

    @When("^he tries to log in$")
    public void login() {
        loginPage.login(username, isAdmin);
    }

    @Then("^the forbidden page is displayed$")
    public void checkForbiddenPageIsDisplayed() {
        assertThat(forbiddenPage.isDisplayed()).isTrue();
    }

    private void resetUser() {
        this.browser.manage().deleteAllCookies();
    }

    private void generateUsername() {
        this.username = UUID.randomUUID().toString() + "@jreader.com";
    }

}
