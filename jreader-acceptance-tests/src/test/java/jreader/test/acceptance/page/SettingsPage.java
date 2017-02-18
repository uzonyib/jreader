package jreader.test.acceptance.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jreader.test.acceptance.page.element.ArchiveForm;
import jreader.test.acceptance.page.element.ArchiveSettingsForm;
import jreader.test.acceptance.page.element.GroupForm;
import jreader.test.acceptance.page.element.SubscriptionForm;
import jreader.test.acceptance.page.element.SubscriptionSettingsForm;

@Component
public class SettingsPage {

    @FindBy(id = "settings-contents")
    private WebElement mainContent;

    @Autowired
    private GroupForm groupForm;
    @Autowired
    private SubscriptionForm subscriptionForm;
    @Autowired
    private SubscriptionSettingsForm subscriptionSettingsForm;

    @Autowired
    private ArchiveForm archiveForm;
    @Autowired
    private ArchiveSettingsForm archiveSettingsForm;

    @Autowired
    public SettingsPage(WebDriver browser) {
        PageFactory.initElements(browser, this);
    }

    public WebElement getMainContent() {
        return mainContent;
    }

    public GroupForm getGroupForm() {
        return groupForm;
    }

    public SubscriptionForm getSubscriptionForm() {
        return subscriptionForm;
    }

    public SubscriptionSettingsForm getSubscriptionSettingsForm() {
        return subscriptionSettingsForm;
    }

    public ArchiveForm getArchiveForm() {
        return archiveForm;
    }

    public ArchiveSettingsForm getArchiveSettingsForm() {
        return archiveSettingsForm;
    }

    public boolean isDisplayed() {
        return mainContent != null && mainContent.isDisplayed();
    }

    public void createGroup(String title) {
        groupForm.enterTitle(title);
        groupForm.clickCreateButton();
        subscriptionSettingsForm.waitForGroupToBeCreated(title);
    }

    public void createArchive(String title) {
        archiveForm.enterTitle(title);
        archiveForm.clickCreateButton();
        archiveSettingsForm.waitForArchiveToBeCreated(title);
    }

}
