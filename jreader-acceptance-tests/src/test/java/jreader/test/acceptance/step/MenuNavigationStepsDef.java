package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.ItemsPage;
import jreader.test.acceptance.page.SettingsPage;
import jreader.test.acceptance.page.element.Menu;

import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MenuNavigationStepsDef {
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private Menu menu = new Menu(browser);
    private HomePage homePage = new HomePage(browser);
    private SettingsPage settingsPage = new SettingsPage(browser);
    private ItemsPage itemsPage = new ItemsPage(browser);
    
    @Then("^the home page should be displayed$")
    public void the_home_page_should_be_displayed() {
        assertThat(homePage.isDisplayed()).isTrue();
        assertThat(settingsPage.isDisplayed()).isFalse();
        assertThat(itemsPage.isDisplayed()).isFalse();
    }
    
    @Then("^the menu should be displayed$")
    public void the_menu_should_be_displayed() {
        assertThat(menu.isDisplayed()).isTrue();
    }
    
    @Then("^the logout menu item should be displayed$")
    public void the_logout_menu_item_should_be_displayed() {
        assertThat(menu.getLogoutMenuItem().isDisplayed()).isTrue();
    }

    @Then("^the home menu item should be displayed$")
    public void the_home_menu_item_should_be_displayed() {
        assertThat(menu.getHomeMenuItem().isDisplayed()).isTrue();
    }

    @Then("^the settings menu item should be displayed$")
    public void the_settings_menu_item_should_be_displayed() {
        assertThat(menu.getSettingsMenuItem().isDisplayed()).isTrue();
    }

    @Then("^the all items menu item should be displayed$")
    public void the_all_items_menu_item_should_be_displayed() {
        assertThat(menu.getAllItemsMenuItem().isDisplayed()).isTrue();
    }
    
    @When("^I select the settings menu item$")
    public void i_select_the_settings_menu_item() {
        menu.getSettingsMenuItem().click();
    }

    @Then("^the settings page should be displayed$")
    public void the_settings_page_should_be_displayed() {
        assertThat(homePage.isDisplayed()).isFalse();
        assertThat(settingsPage.isDisplayed()).isTrue();
        assertThat(itemsPage.isDisplayed()).isFalse();
    }
    
    @When("^I select the home menu item$")
    public void i_select_the_home_menu_item() {
        menu.getHomeMenuItem().click();
    }
    
    @When("^I select the all items menu item$")
    public void i_select_the_all_items_menu_item() {
        menu.getAllItemsMenuItem().click();
    }

    @Then("^the all items page should be displayed$")
    public void the_all_items_page_should_be_displayed() {
        assertThat(homePage.isDisplayed()).isFalse();
        assertThat(settingsPage.isDisplayed()).isFalse();
        assertThat(itemsPage.isDisplayed()).isTrue();
    }

}
