package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.page.HomePage;
import jreader.test.acceptance.page.element.GroupForm;
import jreader.test.acceptance.page.element.Menu;
import jreader.test.acceptance.page.element.SubscriptionForm;
import jreader.test.acceptance.page.element.SubscriptionSettingsForm;

public class GroupStepsDef extends StepDefs {

    @Autowired
    private Menu menu;
    @Autowired
    private HomePage homePage;
    @Autowired
    private GroupForm groupForm;
    @Autowired
    private SubscriptionForm subscriptionForm;
    @Autowired
    private SubscriptionSettingsForm subscriptionSettingsForm;

    private String groupTitle;
    private List<String> groupTitles = new ArrayList<String>();

    @When("^the user enters \"([^\"]*)\" as the group title$")
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

    @Then("^group \"([^\"]*)\" is displayed on the home page$")
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

    @When("^he creates the following groups:$")
    public void createGroups(List<String> titles) {
        for (String title : titles) {
            groupTitles.add(title);
            groupForm.createGroup(title);
        }
    }

    @When("^the user opens the group dropdown$")
    public void openGroupDropdown() {
        subscriptionForm.openGroupDropdown();
    }

    @Then("^the following groups are displayed in the menu:$")
    public void checkGroupsAreDisplayedInTheMenu(Map<Integer, String> groups) {
        for (Entry<Integer, String> entry : groups.entrySet()) {
            assertThat(menu.getGroupMenuItems().get(entry.getKey() - 1).getText()).isEqualTo(entry.getValue());
        }
    }

    @Then("^the following groups are displayed in the subscription settings:$")
    public void checkGroupsAreDisplayedInTheSubscriptionSettings(Map<Integer, String> groups) {
        for (Entry<Integer, String> entry : groups.entrySet()) {
            assertThat(subscriptionSettingsForm.getGroupItems().get(entry.getKey() - 1).getText()).isEqualTo(entry.getValue());
        }
    }

    @Then("^the following groups are displayed in the group field of the new subscription form:$")
    public void checkGroupsAreDisplayedInTheGroupFieldOfTheNewSubscriptionForm(Map<Integer, String> groups) {
        for (Entry<Integer, String> entry : groups.entrySet()) {
            assertThat(subscriptionForm.getGroupOptions().get(entry.getKey() - 1).getText()).isEqualTo(entry.getValue());
        }
    }

    @Then("^the following groups are displayed on the home page:$")
    public void checkGroupsAreDisplayedOnTheHomePage(Map<Integer, String> groups) {
        for (Entry<Integer, String> entry : groups.entrySet()) {
            assertThat(homePage.getGroupItems().get(entry.getKey() - 1).getText()).isEqualTo(entry.getValue());
        }
    }

    @When("^he moves group \"([^\"]*)\" down:$")
    public void moveDown(String title) {
        subscriptionSettingsForm.moveGroupDown(title);
    }

    @When("^the user moves group \"([^\"]*)\" up:$")
    public void moveUp(String title) {
        subscriptionSettingsForm.moveGroupUp(title);
    }

    @When("^the user renames group \"([^\"]*)\" to \"([^\"]*)\":$")
    public void rename(String from, String to) {
        subscriptionSettingsForm.renameGroup(from, to);
    }

    @When("^the user deletes group \"([^\"]*)\":$")
    public void delete(String title) {
        subscriptionSettingsForm.deleteGroup(title);
    }

}
