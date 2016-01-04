Feature: Administrators can log in, log out and use the menu for navigation

	Scenario: Users cannot reach the user administration page without authentication
		When the user opens the user administration page
		Then the forbidden page is displayed

	Scenario: Unauthorized users cannot access the user administration page
		Given "nonadmin@jreader.com" is not an administrator
		And he is logged in
		When he opens the user administration page
		Then the forbidden page is displayed

	Scenario: Administrators can log in and then they can access the user administration page
		Given "admin@jreader.com" is an administrator
		And he is logged in
		When he opens the user administration page
		Then the user administration page is displayed
		And the user role table is displayed
		And the following users, roles and actions are listed:
		| admin@jreader.com    | ADMIN        | user  | unauthorized | 
		| nonadmin@jreader.com | UNAUTHORIZED | admin | user         |
		
	Scenario: Administrators can authorize access for other users
		When he clicks the "user" button next to "nonadmin@jreader.com"
		Then the following users, roles and actions are listed:
		| admin@jreader.com    | ADMIN | user  | unauthorized | 
		| nonadmin@jreader.com | USER  | admin | unauthorized |

	Scenario: Users authorized by administrators can access the main page
		Given "nonadmin@jreader.com" is not an administrator
		And he is on the login page
		When he tries to log in
		Then the home page is displayed
		And the menu is displayed
		And the logout menu item is displayed
		And the home menu item is displayed
		And the settings menu item is displayed
		And the all items menu item is displayed
		
	Scenario: Non-administrator users cannot the user administration page
		Given "nonadmin@jreader.com" is not an administrator
		And he is logged in
		When he opens the user administration page
		Then the forbidden page is displayed
		
	Scenario: Administrators can revoke access from other users
		Given "admin@jreader.com" is an administrator
		And he is logged in
		And he is on the user administration page
		When he clicks the "unauthorized" button next to "nonadmin@jreader.com"
		Then the following users, roles and actions are listed:
		| admin@jreader.com    | ADMIN        | user  | unauthorized | 
		| nonadmin@jreader.com | UNAUTHORIZED | admin | user         |
		
	Scenario: Users unauthorized by administrators cannot access the application
		Given "nonadmin@jreader.com" is not an administrator
		And he is on the login page
		When he tries to log in
		Then the forbidden page is displayed
	