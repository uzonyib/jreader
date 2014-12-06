Feature: menu navigation

	Scenario: After log in the home page and the menu appear
		When I log in
		Then the home page should be displayed
		And the menu should be displayed
		And the logout menu item should be displayed
		And the home menu item should be displayed
		And the settings menu item should be displayed
		And the all items menu item should be displayed

	Scenario: Users can navigate to the settings page
		Given I am logged in
		When I select the settings menu item
		Then the settings page should be displayed

	Scenario: Users can navigate back to the home page
		Given I am logged in
		And I am on the settings page
		When I select the home menu item
		Then the home page should be displayed

	Scenario: Users can navigate to the all items page
		Given I am logged in
		When I select the all items menu item
		Then the all items page should be displayed
