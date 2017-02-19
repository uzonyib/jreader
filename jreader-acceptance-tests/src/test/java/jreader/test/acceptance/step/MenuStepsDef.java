package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.ItemsPage;
import jreader.test.acceptance.page.SettingsPage;
import jreader.test.acceptance.page.element.Menu;

public class MenuStepsDef extends StepDefs {

    @Autowired
    private Menu menu;
    @Autowired
    private HomePage homePage;
    @Autowired
    private SettingsPage settingsPage;
    @Autowired
    private ItemsPage itemsPage;

    @When("^he selects the settings menu item$")
    public void selectSettingsMenuItem() {
        menu.getSettingsMenuItem().click();
    }

    @When("^he selects the home menu item$")
    public void selectHomeMenuItem() {
        menu.getHomeMenuItem().click();
    }

    @When("^he selects the all items menu item$")
    public void selectAllItemsMenuItem() {
        menu.getAllItemsMenuItem().click();
    }

    @When("^he clicks on the logout menu item$")
    public void logout() {
        menu.logout();
    }

    @When("^(the user|he) navigates to the settings page$")
    public void navigateToSettingsPage(String user) {
        menu.openSettingsPage();
    }

    @When("^(the user|he) navigates to the home page$")
    public void navigateToHomePage(String user) {
        menu.openHomePage();
    }

    @Then("^the home page is displayed$")
    public void checkHomePageIsDisplayed() {
        assertThat(homePage.isDisplayed()).isTrue();
        assertThat(settingsPage.isDisplayed()).isFalse();
        assertThat(itemsPage.isDisplayed()).isFalse();
    }

    @Then("^the settings page is displayed$")
    public void checkSettingsPageIsDisplayed() {
        assertThat(homePage.isDisplayed()).isFalse();
        assertThat(settingsPage.isDisplayed()).isTrue();
        assertThat(itemsPage.isDisplayed()).isFalse();
    }

    @Then("^the all items page is displayed$")
    public void checkAllItemsPageIsDisplayed() {
        assertThat(homePage.isDisplayed()).isFalse();
        assertThat(settingsPage.isDisplayed()).isFalse();
        assertThat(itemsPage.isDisplayed()).isTrue();
    }

    @Then("^the menu is displayed$")
    public void checkMenuIsDisplayed() {
        assertThat(menu.isDisplayed()).isTrue();
    }

    @Then("^the logout menu item is displayed$")
    public void checkLogoutMenuItemIsDisplayed() {
        assertThat(menu.getLogoutButton().isDisplayed()).isTrue();
    }

    @Then("^the home menu item is displayed$")
    public void checkHomeMenuItemIsDisplayed() {
        assertThat(menu.getHomeMenuItem().isDisplayed()).isTrue();
    }

    @Then("^the settings menu item is displayed$")
    public void checkSettingsMenuItemIsDisplayed() {
        assertThat(menu.getSettingsMenuItem().isDisplayed()).isTrue();
    }

    @Then("^the all items menu item is displayed$")
    public void checkAllItemsMenuItemIsDisplayed() {
        assertThat(menu.getAllItemsMenuItem().isDisplayed()).isTrue();
    }

    @When("^the user expands the \"([^\"]*)\" group menu item$")
    public void expandGroupMenuItem(String title) {
        menu.expandGroup(title);
    }

    @Then("^the subscription \"([^\"]*)\" is displayed in the menu$")
    public void checkSubscriptionMenuItemIsDisplayed(String title) {
        assertThat(menu.getSubscriptionMenuItem(title).isDisplayed()).isTrue();
    }

    @Then("^the following subscriptions are displayed in the menu:$")
    public void checkSubscriptionMenuItemsAreDisplayed(List<String> subscriptionTitles) {
        for (String title : subscriptionTitles) {
            checkSubscriptionMenuItemIsDisplayed(title);
        }
    }

}
