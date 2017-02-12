package jreader.test.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import jreader.test.acceptance.page.UserAdminPage;

public class UserAdminStepsDef extends StepDefs {

    @Autowired
    private UserAdminPage userAdminPage;

    @When("^(the user|he) opens the user administration page$")
    public void openUserAdminPage(String subject) {
        userAdminPage.open();
    }

    @Then("^the user administration page is displayed$")
    public void checkUserAdminPageIsDisplayed() {
        assertThat(userAdminPage.isDisplayed()).isTrue();
    }

    @Then("^the user role table is displayed$")
    public void checkUserRoleTableIsDisplayed() {
        assertThat(userAdminPage.getUserRoleTable().isDisplayed()).isTrue();
    }

    @Then("^the following users, roles and actions are listed:$")
    public void checkUserRoleTable(List<List<String>> table) {
        for (List<String> row : table) {
            String username = row.get(0);
            assertThat(userAdminPage.getUserRoleTable().containsUsername(username)).isTrue();
            assertThat(userAdminPage.getUserRoleTable().getRole(username)).isEqualTo(row.get(1));
            for (int i = 2; i < row.size(); ++i) {
                assertThat(userAdminPage.getUserRoleTable().containsForm(username, row.get(i)));
            }
        }
    }

    @When("^he clicks the \"(.*?)\" button next to \"(.*?)\"$")
    public void changeRole(String role, String username) {
        userAdminPage.getUserRoleTable().changeRole(username, role);
    }

    @Given("^he is on the user administration page$")
    public void openUserAdminPage() {
        userAdminPage.open();
    }

}
