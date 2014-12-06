package jreader.test.acceptance.step;

import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.page.element.Menu;

import org.openqa.selenium.WebDriver;

import cucumber.api.java.en.When;

public class LogoutStepsDef {
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private Menu menu = new Menu(browser);
    
    @When("^I click on the logout menu item$")
    public void i_click_on_the_logout_menu_item() {
        menu.logout();
    }

}
