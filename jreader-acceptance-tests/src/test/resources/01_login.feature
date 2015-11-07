Feature: Administrators can log in, log out and use the menu for navigation

	Scenario: Users cannot reach the main page without authentication
		When the user opens the main page
		Then the login page is displayed

	Scenario: Non-administrator users cannot log in
		Given "nonadmin" is not an administrator
		And he is on the login page
		When he tries to log in
		Then the forbidden page is displayed

	Scenario: Administrators can log in and then they can see the main screen
		Given "admin" is an administrator
		And he is on the login page
		When he tries to log in
		Then the home page is displayed
		And the menu is displayed
		And the logout menu item is displayed
		And the home menu item is displayed
		And the settings menu item is displayed
		And the all items menu item is displayed

	Scenario: Users can navigate to the settings page
		When he selects the settings menu item
		Then the settings page is displayed

	Scenario: Users can navigate back to the home page
		When he selects the home menu item
		Then the home page is displayed

	Scenario: Users can navigate to the all items page
		When he selects the all items menu item
		Then the all items page is displayed

	Scenario: Logged-in users can log out
		When he clicks on the logout menu item
		Then the login page is displayed
