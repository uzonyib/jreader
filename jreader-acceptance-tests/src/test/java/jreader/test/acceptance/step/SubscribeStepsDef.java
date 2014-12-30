package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.element.GroupForm;
import jreader.test.acceptance.page.element.Menu;
import jreader.test.acceptance.page.element.SubscriptionSettingsForm;
import jreader.test.acceptance.page.element.SubscriptionForm;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SubscribeStepsDef {
    
private WebDriver browser = BrowserManager.getBrowser();
    
    private Menu menu = new Menu(browser);
    private HomePage homePage = new HomePage(browser);
    private GroupForm groupForm = new GroupForm(browser);
    private SubscriptionForm subscriptionForm = new SubscriptionForm(browser);
    private SubscriptionSettingsForm editForm = new SubscriptionSettingsForm(browser);
    
    @Given("^I have groups \"(.*?)\"$")
    public void i_have_groups(List<String> titles) {
        menu.openSettingsPage();
    }
    
    @Then("^I should see a form for subscribing to a feed$")
    public void i_should_see_a_form_for_subscribing_to_a_feed() {
        assertThat(subscriptionForm.getGroupDropdown().isDisplayed()).isTrue();
        assertThat(subscriptionForm.getUrlField().isDisplayed()).isTrue();
        assertThat(subscriptionForm.getSubscribeButton().isDisplayed()).isTrue();
    }

    @Then("^I can select \"(.*?)\" for group$")
    public void i_can_select_for_group(List<String> titles) {
        assertThat(subscriptionForm.getGroupOptions()).hasSameSizeAs(titles);
        for (int i = 0; i < titles.size(); ++i) {
            assertThat(subscriptionForm.getGroupOptions().get(i).getText()).isEqualTo(titles.get(i));
        }
    }

    @Then("^by default \"(.*?)\" is selected$")
    public void by_default_is_selected(String groupTitle) {
        assertThat(subscriptionForm.getSelectedGroupTitle()).isEqualTo(groupTitle);
    }
    
    @When("^I select \"(.*?)\" for group$")
    public void i_select_for_group(String groupTitle) {
        subscriptionForm.selectGroupTitle(groupTitle);
    }

    @When("^I enter \"(.*?)\" as the feed URL$")
    public void i_enter_as_the_feed_URL(String url) {
        subscriptionForm.enterUrl(url);
    }

    @When("^I click the subscribe button$")
    public void i_click_the_subscribe_button() {
        subscriptionForm.clickSubscribeButton();
        subscriptionForm.waitForSubscriptionToBeCreated();
    }

    @Then("^I should see \"(.*?)\" as the number of unread items next to group \"(.*?)\" in the menu$")
    public void i_should_see_as_the_number_of_unread_items_next_to_group_in_the_menu(String count, String groupTitle) {
        WebElement unreadCount = menu.getGroupUnreadCount(groupTitle);
        assertThat(unreadCount.isDisplayed()).isTrue();
        assertThat(unreadCount.getText()).isEqualTo("(" + count + ")");
    }

    @Then("^I should see the new subscription entitled \"(.*?)\" on the settings page under \"(.*?)\"$")
    public void i_should_see_the_new_subscription_entitled_on_the_settings_page(String title, String groupTitle) {
        assertThat(editForm.getSubscriptionTitleElement(title, groupTitle).getText()).isNotNull();
    }

    @Then("^I should see the new subscription entitled \"(.*?)\" on the home page under \"(.*?)\"$")
    public void i_should_see_the_new_subscription_entitled_on_the_home_page(String title, String groupTitle) {
        
    }

}
