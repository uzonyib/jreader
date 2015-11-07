package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.BrowserManager;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.element.GroupForm;
import jreader.test.acceptance.page.element.Menu;
import jreader.test.acceptance.page.element.SubscriptionForm;
import jreader.test.acceptance.page.element.SubscriptionSettingsForm;

public class GroupStepsDef {
    
    private WebDriver browser = BrowserManager.getBrowser();
    
    private Menu menu = new Menu(browser);
    private HomePage homePage = new HomePage(browser);
    private GroupForm groupForm = new GroupForm(browser);
    private SubscriptionForm subscriptionForm = new SubscriptionForm(browser);
    private SubscriptionSettingsForm subscriptionSettingsForm = new SubscriptionSettingsForm(browser);
    
    private String groupTitle;
    private List<String> groupTitles = new ArrayList<String>();
    
    @When("^he enters \"(.*?)\" as the group title$")
    public void enterGroupTitle(String title) {
        this.groupTitle = title;
        groupForm.enterTitle(groupTitle);
    }
    
    @When("^he clicks the create group button$")
    public void clickCreateGroupBbutton() {
        groupForm.clickCreateButton();
        groupForm.waitForGroupToBeCreated(this.groupTitle);
        groupTitles.add(this.groupTitle);
    }
    
    @Then("^the group title field is displayed$")
    public void checkGroupTitleFieldIsDisplayed() {
        assertThat(groupForm.getTitleField().isDisplayed()).isTrue();
    }
    
    @Then("^the create group button is displayed$")
    public void checkCreateGroupButtonIsDisplayed() {
        assertThat(groupForm.getCreateButton().isDisplayed()).isTrue();
    }
    
    @Then("^the new group is displayed in the menu$")
    public void checkGroupIsDisplayedInTheMenu() {
        int groupMenuItemCount = menu.getGroupMenuItems().size();
        assertThat(groupMenuItemCount).isGreaterThanOrEqualTo(1);
        assertThat(menu.getGroupMenuItems().get(groupMenuItemCount - 1).getText()).isEqualTo(this.groupTitle);
    }
    
    @Then("^the new group is displayed in the subscription settings$")
    public void checkGroupIsDisplayedInTheMenuSubscriptionSettings() {
        int groupItemCount = subscriptionSettingsForm.getGroupItems().size();
        assertThat(groupItemCount).isGreaterThanOrEqualTo(1);
        assertThat(subscriptionSettingsForm.getGroupItems().get(groupItemCount - 1).getText()).isEqualTo(this.groupTitle);
    }
    
    @Then("^the new group is displayed in the group field of the new subscription form$")
    public void checkGroupIsDisplayedInTheMenuSubscriptionSettingsGroupFieldOfTheNewSubscriptionForm() {
        subscriptionForm.openGroupDropdown();
        List<WebElement> options = subscriptionForm.getGroupOptions();
        assertThat(options.size()).isGreaterThanOrEqualTo(1);
        assertThat(options.get(options.size() - 1).getText()).isEqualTo(this.groupTitle);
    }
    
    @Then("^group \"(.*?)\" is displayed on the home page$")
    public void checkGroupIsDisplayedOnTheHomePage(String title) {
        int groupItemCount = homePage.getGroupItems().size();
        assertThat(groupItemCount).isGreaterThanOrEqualTo(1);
        
        boolean groupFound = false;
        for (WebElement groupMenuItem : homePage.getGroupItems()) {
            if (title.equals(groupMenuItem.getText())) {
                groupFound = true;
            }
        }
        assertThat(groupFound).isTrue();
    }
    
    @When("^he creates group \"(.*?)\"$")
    public void createGroup(String title) {
        this.groupTitles.add(title);
        groupForm.createGroup(title);
    }
    
    @When("^he opens the group dropdown$")
    public void openGroupDropdown() {
        subscriptionForm.openGroupDropdown();
    }
    
    @Then("^group \"(.*?)\" is displayed in the (\\d+)\\. position in the menu$")
    public void checkGroupIsDisplayedInTheMenu(String title, int index) {
        assertThat(menu.getGroupMenuItems().get(index - 1).getText()).isEqualTo(title);
    }

    @Then("^group \"(.*?)\" is displayed in the (\\d+)\\. position in the subscription settings$")
    public void checkGroupIsDisplayedInTheSubscriptionSettings(String title, int index) {
        assertThat(subscriptionSettingsForm.getGroupItems().get(index - 1).getText()).isEqualTo(title);
    }
    
    @Then("^group \"(.*?)\" is displayed in the (\\d+)\\. position in the group field of the new subscription form$")
    public void checkGroupIsDisplayedInTheGroupFieldOfTheNewSubscriptionForm(String title, int index) {
        List<WebElement> options = subscriptionForm.getGroupOptions();
        assertThat(options.get(index - 1).getText()).isEqualTo(title);
    }

    @Then("^group \"(.*?)\" is displayed in the (\\d+)\\. position$")
    public void checkGroupIsDisplayedOnTheHomePage(String title, int index) {
        assertThat(homePage.getGroupItems().get(index - 1).getText()).isEqualTo(title);
    }

}
