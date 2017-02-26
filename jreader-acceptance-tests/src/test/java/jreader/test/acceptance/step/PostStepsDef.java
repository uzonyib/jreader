package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.page.ArchivesPage;
import jreader.test.acceptance.page.ItemsPage;
import jreader.test.acceptance.page.SettingsPage;
import jreader.test.acceptance.page.element.Menu;

public class PostStepsDef extends StepDefs {

    @Autowired
    private Menu menu;
    @Autowired
    private ItemsPage itemsPage;
    @Autowired
    private ArchivesPage archivesPage;
    @Autowired
    private SettingsPage settingsPage;

    @Then("^he can see the following posts:$")
    public void checkPosts(DataTable table) {
        List<List<String>> rows = table.raw();
        for (int i = 0; i < rows.size(); ++i) {
            List<String> row = rows.get(i);
            assertThat(itemsPage.getGroupOfPostAt(i)).isEqualTo(row.get(0));
            assertThat(itemsPage.getTitleOfPostAt(i)).isEqualTo(row.get(1));
        }
    }

    @Then("^the unread count of all groups in (\\d+)$")
    public void checkAllItemsUnreadCount(String count) {
        assertThat(menu.getAllItemsUnreadCount()).isEqualTo(count);
    }

    @Then("^the unread counts of groups are:$")
    public void checkGroupUnreadCounts(DataTable table) {
        for (List<String> row : table.raw()) {
            assertThat(menu.getGroupUnreadCount(row.get(0))).isEqualTo(row.get(1));
        }
    }

    @Then("^the unread counts of subscriptions are:$")
    public void checkSubscriptionUnreadCounts(DataTable table) {
        for (List<String> row : table.raw()) {
            assertThat(menu.getSubscriptionUnreadCount(row.get(0))).isEqualTo(row.get(1));
        }
    }

    @When("^the user clicks post title \"([^\"]*)\"$")
    @Given("^he opened the details of \"([^\"]*)\"$")
    public void selectPost(String title) {
        itemsPage.openPost(title);
    }

    @Then("^post description \"([^\"]*)\" is displayed$")
    public void checkDescriptionDisplayed(String description) {
        assertThat(itemsPage.isDescriptionDisplayed(description)).isTrue();
    }

    @Then("^author \"([^\"]*)\" is displayed$")
    public void checkAuthorDisplayed(String author) {
        assertThat(itemsPage.isAuthorDisplayed(author)).isTrue();
    }

    @Then("^a link pointing to \"([^\"]*)\" is displayed$")
    public void checkLinkDisplayed(String url) {
        assertThat(itemsPage.isLinkDisplayed(url)).isTrue();
    }

    @When("^(?:the user|he) clicks the refresh posts button$")
    public void refreshPosts() {
        itemsPage.refresh();
    }

    @When("^the user bookmarks the following posts:$")
    public void bookmarkPost(List<String> titles) {
        for (String title : titles) {
            itemsPage.bookmarkPost(title);
        }
    }

    @When("^he opens the bookmarked posts view$")
    public void selectBookmarkedPosts() {
        itemsPage.openBookmarkedPosts();
    }

    @When("^the user deletes bookmark of post \"([^\"]*)\"$")
    public void deleteBookmarkOfPost(String title) {
        itemsPage.deleteBookmarkOfPost(title);
    }

    @Given("^the user is on the unread posts view$")
    public void selectUnreadPosts() {
        itemsPage.openUnreadPosts();
    }

    @When("^he clicks the mark all posts read button$")
    public void markAllPostsRead() {
        itemsPage.markAllPostsRead();
    }

    @Then("^no posts are displayed after the view refreshes$")
    public void checkNoPostsAreDisplayed() {
        assertThat(itemsPage.getNumberOfPostsDisplayed()).isEqualTo(0);
    }

    @When("^the user opens the all posts view$")
    public void selectAllPosts() {
        itemsPage.openAllPosts();
    }

    @When("^the user clicks the descending order button$")
    public void orderPostsDescending() {
        itemsPage.orderPostsDescending();
    }

    @Given("^the user has the following archives:$")
    public void createArchives(List<String> titles) {
        menu.openSettingsPage();
        for (String title : titles) {
            settingsPage.createArchive(title);
        }
    }

    @Given("^he is viewing all posts$")
    public void openUnreadPostsPage() {
        menu.openAllItemsPage();
        itemsPage.openAllPosts();
    }

    @When("^he selects archive \"([^\"]*)\"$")
    public void selectArchive(String title) {
        itemsPage.selectArchive(title);
    }

    @When("^clicks the archive button$")
    public void clickArchiveButton() {
        itemsPage.clickArchiveButton();
    }

    @Then("^the message \"([^\"]*)\" is displayed$")
    public void checkArchiveMessage(String message) {
        assertThat(itemsPage.isArchiveMessageVisible(message)).isTrue();
    }

    @Given("^the user archived post \"([^\"]*)\" to \"([^\"]*)\"$")
    public void archive(String post, String archive) {
        itemsPage.refresh();
        itemsPage.archive(post, archive);
    }

    @Then("^he can see the following archived posts:$")
    public void checkArchivedPosts(List<String> titles) {
        for (int i = 0; i < titles.size(); ++i) {
            assertThat(archivesPage.getTitleOfPostAt(i)).isEqualTo(titles.get(i));
        }
    }

    @When("^the user clicks archived post title \"([^\"]*)\"$")
    public void selectArchivedPost(String title) {
        archivesPage.openPost(title);
    }

    @Then("^archived post description \"([^\"]*)\" is displayed$")
    public void checkArchivedDescriptionDisplayed(String description) {
        assertThat(archivesPage.isDescriptionDisplayed(description)).isTrue();
    }

    @Then("^archived post author \"([^\"]*)\" is displayed$")
    public void checkArchivedAuthorDisplayed(String author) {
        assertThat(archivesPage.isAuthorDisplayed(author)).isTrue();
    }

    @Then("^an archived post link pointing to \"([^\"]*)\" is displayed$")
    public void checkArchivedLinkDisplayed(String url) {
        assertThat(archivesPage.isLinkDisplayed(url)).isTrue();
    }

    @When("^clicks the delete post button of \"([^\"]*)\"$")
    public void deleteArchivedPost(String title) {
        archivesPage.deletePost(title);
    }

}
