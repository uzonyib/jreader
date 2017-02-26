package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.page.SettingsPage;

public class BackupStepsDef extends StepDefs {

    @Autowired
    private WebDriver browser;

    @Autowired
    private SettingsPage settingsPage;

    private static String json;

    @When("^he clicks the export button$")
    public void clickExportButton() {
        settingsPage.getBackupForm().export();
    }

    @Then("^a JSON containing his subscriptions is generated$")
    public void checkJson() {
        json = settingsPage.getBackupForm().getJson();
        assertThat(json).isNotEmpty();
    }

    @When("^he enters the JSON$")
    public void enterJson() {
        settingsPage.getBackupForm().enterJson(json);
    }

    @When("^he clicks the import button$")
    public void clickImportButton() throws InterruptedException {
        settingsPage.getBackupForm().importJson();
        Thread.sleep(5000);
    }

    @When("^he refreshes the page$")
    public void refreshPage() {
        browser.navigate().refresh();
    }

    @Then("^the following groups and subscriptions are created for him:$")
    public void checkSubscriptions(DataTable table) {
        for (List<String> row : table.raw()) {
            settingsPage.getSubscriptionSettingsForm().waitForGroupToBeCreated(row.get(0));
            settingsPage.getSubscriptionSettingsForm().waitForSubscriptionToBeCreated(row.get(1));
        }
    }

}
