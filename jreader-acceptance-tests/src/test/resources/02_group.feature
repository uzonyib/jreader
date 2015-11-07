Feature: create subscription group

	Scenario: Users can create new groups
		Given a new user is logged in
		When he navigates to the settings page
		Then the group title field is displayed
		And the create group button is displayed

	Scenario: Newly created groups appear in the menu and on the settings page
		When he enters "test group 1" as the group title
		And he clicks the create group button
		Then the new group is displayed in the menu
		And the new group is displayed in the subscription settings
		And the new group is displayed in the group field of the new subscription form

	Scenario: Newly created groups appear on the home page
		When he navigates to the home page
		Then group "test group 1" is displayed on the home page

	Scenario: By default, groups are listed in creation order in the menu and on the settings page
		When he navigates to the settings page
		And he creates group "test group 2"
		And he creates group "test group 3"
		Then group "test group 1" is displayed in the 1. position in the menu
		And group "test group 2" is displayed in the 2. position in the menu
		And group "test group 3" is displayed in the 3. position in the menu
		And group "test group 1" is displayed in the 1. position in the subscription settings
		And group "test group 2" is displayed in the 2. position in the subscription settings
		And group "test group 3" is displayed in the 3. position in the subscription settings
		
	Scenario: By default, groups are listed in creation order on the new subscription form
		When he opens the group dropdown
		Then group "test group 1" is displayed in the 1. position in the group field of the new subscription form
		And group "test group 2" is displayed in the 2. position in the group field of the new subscription form
		And group "test group 3" is displayed in the 3. position in the group field of the new subscription form

	Scenario: By default, groups are listed in creation order on the home page
		When he navigates to the home page
		Then group "test group 1" is displayed in the 1. position
		And group "test group 2" is displayed in the 2. position
		And group "test group 3" is displayed in the 3. position
