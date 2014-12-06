Feature: create subscription group

	Background:
		Given I am logged in

	Scenario: Users can create new groups
		When I navigate to the settings page
		Then the group title field should be displayed
		And the create group button should be displayed

	Scenario: Newly created groups appear in the menu and on the settings page
		When I navigate to the settings page
		And I enter the group title
		And I click the create group button
		Then the new group should be displayed in the menu
		And the new group should be displayed in the subscription settings
		And the new group should be displayed in the group field of the new subscription form

	Scenario: Newly created groups appear on the home page
		Given I created a group
		When I navigate to the home page
		Then the group should be displayed on the home page

	Scenario: By default, groups are listed in creation order in the menu and on the settings page
		When I create multiple groups
		Then the groups should be displayed in the menu in creation order
		And the groups should be displayed in the subscription settings in creation order
		And the groups should be displayed in the group field of the new subscription form in creation order

	Scenario: By default, groups are listed in creation order on the home page
		When I create multiple groups
		And I navigate to the home page
		Then the groups should be displayed in creation order		
