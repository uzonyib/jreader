Feature: login / logout

	Scenario: Users should not be able to log in without authentication
		When I open the main page URL
		Then I should not see the main page

	Scenario: Non-admin users should not be able to log in
		Given I am on the login page
		When I enter "user@jreader.com" as my email address
		And I press "Log In"
		Then I should not see the main page

	Scenario: Admin users should be able to log in
		Given I am on the login page
		When I enter "admin@jreader.com" as my email address
		And I select that I am an administrator
		And I press "Log In"
		Then I should see the main page
		And I should see the menu

	Scenario: Users should be able to log out
		When I click on the logout menu item 
		Then I should not see the main page
