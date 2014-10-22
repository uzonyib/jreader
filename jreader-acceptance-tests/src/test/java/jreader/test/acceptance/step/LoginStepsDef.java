package jreader.test.acceptance.step;

import jreader.test.acceptance.WebDriverManager;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LoginStepsDef {
    
    private WebDriver browser = WebDriverManager.getBrowser();
    
    @When("^I open the main page URL$")
    public void i_open_the_main_page_url() throws Throwable {
        browser.get(Constants.MAIN_PAGE_URL);
    }
    
    @Then("^I should not see the main page$")
    public void i_should_not_see_the_main_page() throws Throwable {
        Assertions.assertThat(browser.getTitle()).isNotEqualTo(Constants.TITLE);
    }
    
    @Given("^I am on the login page$")
    public void i_am_on_the_login_page() throws Throwable {
        browser.get(Constants.LOGIN_PAGE_URL);
    }

    @When("^I enter \"(.*?)\" as my email address$")
    public void i_enter_as_my_email_address(String email) throws Throwable {
        WebElement emailField = browser.findElement(By.id("email"));
        emailField.clear();
        emailField.sendKeys(email);
    }
    
    @When("^I press \"(.*?)\"$")
    public void i_press(String buttonTitle) throws Throwable {
        browser.findElement(By.xpath("//input[@value='" + buttonTitle + "']")).click();
    }

    @When("^I select that I am an administrator$")
    public void i_select_that_I_am_an_administrator() throws Throwable {
        browser.findElement(By.id("isAdmin")).click();
    }
    
    @Then("^I should see the main page$")
    public void i_should_see_the_main_page() throws Throwable {
        Assertions.assertThat(browser.getTitle()).isEqualTo(Constants.TITLE);
    }

    @Then("^I should see the menu$")
    public void i_should_see_the_menu() throws Throwable {
        Assertions.assertThat(browser.findElement(By.id("logout-menu-item"))).isNotNull();
        Assertions.assertThat(browser.findElement(By.id("home-menu-item"))).isNotNull();
        Assertions.assertThat(browser.findElement(By.id("settings-menu-item"))).isNotNull();
        Assertions.assertThat(browser.findElement(By.id("all-items-menu-item"))).isNotNull();
    }

    @When("^I click on the logout menu item$")
    public void i_click_on_the_logout_menu_item() throws Throwable {
        browser.findElement(By.cssSelector("#logout-menu-item a")).click();
        new WebDriverWait(browser, 5).until(ExpectedConditions.not(ExpectedConditions.titleIs(Constants.TITLE)));
    }

}
