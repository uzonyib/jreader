package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.element.GroupForm;
import jreader.test.acceptance.page.element.Menu;
import jreader.test.acceptance.page.element.SubscriptionForm;
import jreader.test.acceptance.page.element.SubscriptionSettingsForm;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateGroupStepsDef {
    
    private static final String GROUP_TITLE = "Test group";
    private static final String[] GROUP_TITLES = { "Test group 1", "Test group 2", "Test group 3" };
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private Menu menu = new Menu(browser);
    private HomePage homePage = new HomePage(browser);
    private GroupForm groupForm = new GroupForm(browser);
    private SubscriptionForm subscriptionForm = new SubscriptionForm(browser);
    private SubscriptionSettingsForm subscriptionSettingsForm = new SubscriptionSettingsForm(browser);
    
    @When("^I navigate to the settings page$")
    public void i_navigate_to_the_settings_page() {
        menu.openSettingsPage();
    }

    @Then("^the group title field should be displayed$")
    public void the_group_title_field_should_be_displayed() {
        assertThat(groupForm.getTitleField().isDisplayed()).isTrue();
    }
    
    @Then("^the create group button should be displayed$")
    public void the_create_group_button_should_be_displayed() {
        assertThat(groupForm.getCreateButton().isDisplayed()).isTrue();
    }
    
    @When("^I enter the group title$")
    public void i_enter_the_group_title() {
        groupForm.enterTitle(GROUP_TITLE);
    }

    @When("^I click the create group button$")
    public void i_click_the_create_group_button() {
        groupForm.clickCreateButton();
        groupForm.waitForGroupToBeCreated(GROUP_TITLE);
    }

    @Then("^the new group should be displayed in the menu$")
    public void the_new_group_should_be_displayed_in_the_menu() {
        int groupMenuItemCount = menu.getGroupMenuItems().size();
        assertThat(groupMenuItemCount).isGreaterThanOrEqualTo(1);
        assertThat(menu.getGroupMenuItems().get(groupMenuItemCount - 1).getText()).isEqualTo(GROUP_TITLE);
    }

    @Then("^the new group should be displayed in the subscription settings$")
    public void the_new_group_should_be_displayed_in_the_subscription_settings() {
        int groupItemCount = subscriptionSettingsForm.getGroupItems().size();
        assertThat(groupItemCount).isGreaterThanOrEqualTo(1);
        assertThat(subscriptionSettingsForm.getGroupItems().get(groupItemCount - 1).getText()).isEqualTo(GROUP_TITLE);
    }
    
    @Then("^the new group should be displayed in the group field of the new subscription form$")
    public void the_new_group_should_be_displayed_in_the_group_field_of_the_new_subscription_form() {
        subscriptionForm.openGroupDropdown();
        List<WebElement> options = subscriptionForm.getGroupOptions();
        assertThat(options.size()).isGreaterThanOrEqualTo(1);
        assertThat(options.get(options.size() - 1).getText()).isEqualTo(GROUP_TITLE);
    }
    
    @Given("^I created a group$")
    public void i_created_a_group() {
        menu.openSettingsPage();
        groupForm.createGroup(GROUP_TITLE);
    }
    
    @When("^I navigate to the home page$")
    public void i_navigate_to_the_home_page() {
        menu.openHomePage();
    }
    
    @Then("^the group should be displayed on the home page$")
    public void the_group_should_be_displayed_on_the_home_page() {
        int groupItemCount = homePage.getGroupItems().size();
        assertThat(groupItemCount).isGreaterThanOrEqualTo(1);
        assertThat(homePage.getGroupItems().get(groupItemCount - 1).getText()).isEqualTo(GROUP_TITLE);
    }
    
    @When("^I create multiple groups$")
    public void i_create_multiple_groups() {
        menu.openSettingsPage();
        groupForm.createGroups(GROUP_TITLES);
    }

    @Then("^the groups should be displayed in the menu in creation order$")
    public void the_groups_should_be_displayed_in_the_menu_in_creation_order() {
        assertThat(menu.getGroupMenuItems()).hasSameSizeAs(GROUP_TITLES);
        for (int i = 0; i < GROUP_TITLES.length; ++i) {
            assertThat(menu.getGroupMenuItems().get(i).getText()).isEqualTo(GROUP_TITLES[i]);
        }
    }

    @Then("^the groups should be displayed in the subscription settings in creation order$")
    public void the_groups_should_be_displayed_in_the_subscription_settings_in_creation_order() {
        assertThat(subscriptionSettingsForm.getGroupItems()).hasSameSizeAs(GROUP_TITLES);
        for (int i = 0; i < GROUP_TITLES.length; ++i) {
            assertThat(subscriptionSettingsForm.getGroupItems().get(i).getText()).isEqualTo(GROUP_TITLES[i]);
        }
    }
    
    @Then("^the groups should be displayed in the group field of the new subscription form in creation order$")
    public void the_groups_should_be_displayed_in_the_group_field_of_the_new_subscription_form_in_creation_order() {
        subscriptionForm.openGroupDropdown();
        List<WebElement> options = subscriptionForm.getGroupOptions();
        assertThat(options).hasSameSizeAs(GROUP_TITLES);
        for (int i = 0; i < GROUP_TITLES.length; ++i) {
            assertThat(options.get(i).getText()).isEqualTo(GROUP_TITLES[i]);
        }
    }

    @Then("^the groups should be displayed in creation order$")
    public void the_groups_should_be_displayed_in_creation_order() {
        assertThat(homePage.getGroupItems()).hasSameSizeAs(GROUP_TITLES);
        for (int i = 0; i < GROUP_TITLES.length; ++i) {
            assertThat(homePage.getGroupItems().get(i).getText()).isEqualTo(GROUP_TITLES[i]);
        }
    }

}
