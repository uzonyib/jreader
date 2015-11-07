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

public class MenuStepsDef {
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private Menu menu = new Menu(browser);
    private HomePage homePage = new HomePage(browser);
    private SettingsPage settingsPage = new SettingsPage(browser);
    private ItemsPage itemsPage = new ItemsPage(browser);
    
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
    
    @When("^he navigates to the settings page$")
    public void navigateToSettingsPage() {
        menu.openSettingsPage();
    }
    
    @When("^he navigates to the home page$")
    public void navigateToHomePage() {
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
    
}
