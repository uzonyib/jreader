Feature: Logout

	Scenario: Users can log out
		Given I am logged in
		When I click on the logout menu item
		Then the login page should be displayed
