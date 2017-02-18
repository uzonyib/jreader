package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.page.SettingsPage;
import jreader.test.acceptance.page.element.Menu;

public class ArchiveStepsDef extends StepDefs {

    @Autowired
    private Menu menu;
    @Autowired
    private SettingsPage settingsPage;

    private String archiveTitle;
    private List<String> archiveTitles = new ArrayList<String>();

    @Then("^the archive title field is displayed$")
    public void checkArchiveTitleFieldIsDisplayed() {
        assertThat(settingsPage.getArchiveForm().getTitleField().isDisplayed()).isTrue();
    }
    
    @Then("^the create archive button is displayed$")
    public void checkCreateArchiveButtonIsDisplayed() {
        assertThat(settingsPage.getArchiveForm().getCreateButton().isDisplayed()).isTrue();
    }

    @When("^the user enters \"([^\"]*)\" as the archive title$")
    public void enterArchiveTitle(String title) {
        this.archiveTitle = title;
        settingsPage.getArchiveForm().enterTitle(archiveTitle);
    }

    @When("^he clicks the create archive button$")
    public void clickCreateArchiveBbutton() {
        settingsPage.getArchiveForm().clickCreateButton();
        settingsPage.getArchiveSettingsForm().waitForArchiveToBeCreated(this.archiveTitle);
        archiveTitles.add(this.archiveTitle);
    }

    @When("^the user expands the archives menu item$")
    public void expandArchivesMenuItem() {
        menu.expandArchives();
    }

    @Then("^the archive \"([^\"]*)\" is displayed in the menu$")
    public void checkArchiveIsDisplayedInTheMenu(String title) {
        int archiveMenuItemCount = menu.getArchiveMenuItems().size();
        assertThat(archiveMenuItemCount).isGreaterThanOrEqualTo(1);
        assertThat(menu.getArchiveMenuItems().get(archiveMenuItemCount - 1).getText()).isEqualTo(title);
    }

    @Then("^the new archive is displayed in the archive settings$")
    public void checkArchiveIsDisplayedInTheSubscriptionSettings() {
        int archiveItemCount = settingsPage.getArchiveSettingsForm().getArchiveItems().size();
        assertThat(archiveItemCount).isGreaterThanOrEqualTo(1);
        assertThat(settingsPage.getArchiveSettingsForm().getArchiveItems().get(archiveItemCount - 1).getText()).isEqualTo(this.archiveTitle);
    }

    @When("^he creates the following archives:$")
    public void createArchives(List<String> titles) {
        for (String title : titles) {
            archiveTitles.add(title);
            settingsPage.createArchive(title);
        }
    }

    @Then("^the following archives are displayed in the menu:$")
    public void checkArchivesAreDisplayedInTheMenu(Map<Integer, String> archives) {
        for (Entry<Integer, String> entry : archives.entrySet()) {
            assertThat(menu.getArchiveMenuItems().get(entry.getKey() - 1).getText()).isEqualTo(entry.getValue());
        }
    }

    @Then("^the following archives are displayed in the archive settings:$")
    public void checkArchivesAreDisplayedInTheArchiveSettings(Map<Integer, String> archives) {
        for (Entry<Integer, String> entry : archives.entrySet()) {
            assertThat(settingsPage.getArchiveSettingsForm().getArchiveItems().get(entry.getKey() - 1).getText()).isEqualTo(entry.getValue());
        }
    }

    @When("^he moves archive \"([^\"]*)\" down:$")
    public void moveDown(String title) {
        settingsPage.getArchiveSettingsForm().moveArchiveDown(title);
    }

    @When("^the user moves archive \"([^\"]*)\" up:$")
    public void moveUp(String title) {
        settingsPage.getArchiveSettingsForm().moveArchiveUp(title);
    }

    @When("^the user renames archive \"([^\"]*)\" to \"([^\"]*)\":$")
    public void rename(String from, String to) {
        settingsPage.getArchiveSettingsForm().renameArchive(from, to);
    }

    @When("^the user deletes archive \"([^\"]*)\":$")
    public void delete(String title) {
        settingsPage.getArchiveSettingsForm().deleteArchive(title);
    }

}
