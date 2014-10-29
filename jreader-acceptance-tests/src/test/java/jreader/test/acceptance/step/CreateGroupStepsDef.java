package jreader.test.acceptance.step;

import java.util.List;

import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.Menu;
import jreader.test.acceptance.page.SettingsPage;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateGroupStepsDef {
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private Menu menu = new Menu(browser);
    private HomePage homePage = new HomePage(browser);
    private SettingsPage settingsPage = new SettingsPage(browser);
    
    private String newGroupTitle;
    private List<String> newGroupTitles;
    
    @When("^I navigate to the settings page$")
    public void i_navigate_to_the_settings_page() {
        menu.openSettingsPage(settingsPage);
    }

    @Then("^I should see a form for creating a new group$")
    public void i_should_see_a_form_for_creating_a_new_group() {
        Assertions.assertThat(settingsPage.getGroupTitleField().isDisplayed()).isTrue();
        Assertions.assertThat(settingsPage.getCreateGroupButton().isDisplayed()).isTrue();
    }
    
    @When("^I enter \"(.*?)\" as the group title")
    public void i_enter_as_the_group_title(String title) {
        this.newGroupTitle = title;
        settingsPage.enterGroupTitle(newGroupTitle);
    }

    @When("^I click the create group button$")
    public void i_click_the_create_group_button() {
        settingsPage.clickCreateGroup();
        settingsPage.waitForGroupToBeCreated(newGroupTitle);
    }

    @Then("^I should see the new group in the menu$")
    public void i_should_see_the_new_group_in_the_menu() {
        int groupMenuItemCount = menu.getGroupMenuItems().size();
        Assertions.assertThat(groupMenuItemCount).isGreaterThanOrEqualTo(1);
        Assertions.assertThat(menu.getGroupMenuItems().get(groupMenuItemCount - 1).getText()).isEqualTo(newGroupTitle);
    }

    @Then("^I should see the new group on the settings page$")
    public void i_should_see_the_new_group_on_the_settings_page() {
        int groupItemCount = settingsPage.getGroupItems().size();
        Assertions.assertThat(groupItemCount).isGreaterThanOrEqualTo(1);
        Assertions.assertThat(settingsPage.getGroupItems().get(groupItemCount - 1).getText()).isEqualTo(newGroupTitle);
    }
    
    @Then("^I should see the new group on the home page$")
    public void i_should_see_the_new_group_on_the_home_page() {
        menu.openHomePage(homePage);
        int groupItemCount = homePage.getGroupItems().size();
        Assertions.assertThat(groupItemCount).isGreaterThanOrEqualTo(1);
        Assertions.assertThat(homePage.getGroupItems().get(groupItemCount - 1).getText()).isEqualTo(newGroupTitle);
    }
    
    @When("^I create groups \"(.*?)\"$")
    public void i_create_groups(List<String> titles) {
        this.newGroupTitles = titles;
        menu.openSettingsPage(settingsPage);
        settingsPage.createGroups(titles);
    }

    @Then("^I should see the groups in the menu in creation order$")
    public void i_should_see_the_groups_in_the_menu_in_creation_order() {
        Assertions.assertThat(menu.getGroupMenuItems()).hasSameSizeAs(newGroupTitles);
        for (int i = 0; i < newGroupTitles.size(); ++i) {
            Assertions.assertThat(menu.getGroupMenuItems().get(i).getText()).isEqualTo(newGroupTitles.get(i));
        }
    }

    @Then("^I should see the groups on the settings page in creation order$")
    public void i_should_see_the_groups_on_the_settings_page_in_creation_order() {
        Assertions.assertThat(settingsPage.getGroupItems()).hasSameSizeAs(newGroupTitles);
        for (int i = 0; i < newGroupTitles.size(); ++i) {
            Assertions.assertThat(settingsPage.getGroupItems().get(i).getText()).isEqualTo(newGroupTitles.get(i));
        }
    }

    @Then("^I should see the groups on the home page in creation order$")
    public void i_should_see_the_groups_on_the_home_page_in_creation_order() {
        menu.openHomePage(homePage);
        Assertions.assertThat(homePage.getGroupItems()).hasSameSizeAs(newGroupTitles);
        for (int i = 0; i < newGroupTitles.size(); ++i) {
            Assertions.assertThat(homePage.getGroupItems().get(i).getText()).isEqualTo(newGroupTitles.get(i));
        }
    }

}
