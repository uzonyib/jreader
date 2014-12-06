package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.LoginPage;
import jreader.test.acceptance.page.element.Menu;
import jreader.test.acceptance.page.element.SubscriptionSettingsForm;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RenameGroupStepsDef {
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private LoginPage loginPage = new LoginPage(browser);
    private Menu menu = new Menu(browser);
    private HomePage homePage = new HomePage(browser);
    private SubscriptionSettingsForm editForm = new SubscriptionSettingsForm(browser);
    
    public RenameGroupStepsDef() {
        PageFactory.initElements(browser, this);
    }
    
    @Given("^I am on the settings page$")
    public void i_am_on_the_settings_page() {
        menu.openSettingsPage();
    }

    @When("^I click on the title of \"(.*?)\"$")
    public void i_click_on_the_title_of(String title) {
        editForm.editGroupTitle(title);
    }

    @Then("^I should see an input field for the new title defaulted to \"(.*?)\"$")
    public void i_should_see_an_input_field_for_the_new_title_defaulted_to(String title) {
        WebElement titleField = editForm.getGroupTitleFieldForEditing(title);
        assertThat(titleField.isDisplayed()).isTrue();
        assertThat(titleField.getAttribute("value")).isEqualTo(title);
    }
    
}
