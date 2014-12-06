Feature: Login

	Scenario: Users should not be able to reach the main page without authentication
		When I open the main page URL
		Then the login page should be displayed

	Scenario: Non-admin users should not be able to log in
		Given I am not an admin
		And I am on the login page
		When I try to login
		Then the error page should be displayed

	Scenario: Admin users can log in
		Given I am an admin
		And I am on the login page
		When I try to login
		Then the main page should be displayed
