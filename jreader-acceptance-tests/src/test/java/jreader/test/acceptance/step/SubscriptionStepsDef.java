package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.SettingsPage;
import jreader.test.acceptance.page.element.Menu;

public class SubscriptionStepsDef {

    @Autowired
    private Menu menu;
    @Autowired
    private HomePage homePage;
    @Autowired
    private SettingsPage settingsPage;

    @Given("^he is on the settings page$")
    public void navigateToSettingsPage() {
        menu.openSettingsPage();
    }
    
    @Then("^the subscription URL field should be displayed$")
    public void checkSubscriptionUrlFieldIsDisplayed() {
        assertThat(settingsPage.getSubscriptionForm().getUrlField().isDisplayed()).isTrue();
    }
    
    @Then("^the group field dropdown should be displayed$")
    public void the_group_field_dropdown_should_be_displayed() throws Throwable {
        assertThat(settingsPage.getSubscriptionForm().getGroupDropdown().isDisplayed()).isTrue();
    }
    
    @When("^he enters \"([^\"]*)\" as the feed URL$")
    public void enterFeedUrl(String url) {
        settingsPage.getSubscriptionForm().enterUrl(url);
    }
    
    @When("^he selects \"([^\"]*)\" as the group$")
    public void selectGroup(String title) {
        settingsPage.getSubscriptionForm().selectGroupTitle(title);
    }
    
    @When("^he clicks the subscribe button$")
    public void clickSubscribeButton() {
        settingsPage.getSubscriptionForm().clickSubscribeButton();
        settingsPage.getSubscriptionForm().waitForSubscriptionToBeCreated();
    }
    
    @Then("^the subscription \"([^\"]*)\" is displayed in the subscription settings under group \"([^\"]*)\"$")
    public void checkSubscriptionIsDisplayedInSubscriptionSettings(String title, String groupTitle) {
        assertThat(settingsPage.getSubscriptionSettingsForm().getSubscriptionTitleElement(title, groupTitle).getText()).isNotNull();
    }
    
    @Then("^subscription \"([^\"]*)\" is displayed on the home page$")
    public void checkSubscriptionIsDisplayedOnHomePage(String title) {
        assertThat(homePage.isSubscriptionDisplayed(title)).isTrue();
    }
    
    @When("^he subscribes to the following feeds under group \"([^\"]*)\":$")
    public void subscribe(String groupTitle, List<String> feedUrls) {
        for (String feedUrl : feedUrls) {
            enterFeedUrl(feedUrl);
            selectGroup(groupTitle);
            clickSubscribeButton();
        }
    }

    @Then("^the following subscriptions are displayed in the subscription settings under group \"([^\"]*)\":$")
    public void checkSubscriptionsAreDisplayedInSubscriptionSettings(String groupTitle, List<String> subscriptionTitles) {
        for (String subscriptionTitle : subscriptionTitles) {
            checkSubscriptionIsDisplayedInSubscriptionSettings(subscriptionTitle, groupTitle);
        }
    }
    
    @When("^he moves subscription \"([^\"]*)\" of group \"([^\"]*)\" down:$")
    public void moveSubscriptionDown(String title, String groupTitle) {
        settingsPage.getSubscriptionSettingsForm().moveSubscriptionDown(groupTitle, title);
    }
    
    @Given("^the user moves subscription \"([^\"]*)\" of group \"([^\"]*)\" up:$")
    public void moveSubscriptionUp(String title, String groupTitle) {
        settingsPage.getSubscriptionSettingsForm().moveSubscriptionUp(groupTitle, title);
    }
    
    @When("^the user renames subscription \"([^\"]*)\" of group \"([^\"]*)\" to \"([^\"]*)\":$")
    public void renameSubscription(String from, String groupTitle, String to) {
        settingsPage.getSubscriptionSettingsForm().renameSubscription(groupTitle, from, to);
    }
    
    @When("^the user unsubscribes from \"([^\"]*)\" of group \"([^\"]*)\":$")
    public void unsubscribe(String title, String groupTitle) {
        settingsPage.getSubscriptionSettingsForm().deleteSubscription(groupTitle, title);
    }

    @Given("^he subscribed to the following feeds:$")
    public void subscribe(DataTable table) {
        for (List<String> row : table.raw()) {
            enterFeedUrl(row.get(3));
            selectGroup(row.get(0));
            clickSubscribeButton();
            if (!row.get(1).equals(row.get(2))) {
                renameSubscription(row.get(1), row.get(0), row.get(2));
            }
        }
    }

}
