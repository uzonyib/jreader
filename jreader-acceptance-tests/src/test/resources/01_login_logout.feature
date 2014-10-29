Feature: login / logout

	Scenario: Users should not be able to log in without authentication
		When I open the main page URL
		Then I should not see the main page

	Scenario: Non-admin users should not be able to log in
		Given I am on the login page
		When I try to login as a non-admin user
		Then I should not see the main page

	Scenario: Admin users should be able to log in
		Given I am on the login page
		When I try to login as an admin user
		Then I should see the main page
		And I should see the menu

	Scenario: Users should be able to log out
		Given I am logged in as an admin user
		When I click on the logout menu item
		Then I should not see the main page
