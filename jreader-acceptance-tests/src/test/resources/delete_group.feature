Feature: rename group

	Background:
		Given I am logged in
		And I have a group

	Scenario: Users can delete groups which have no subscriptions
		When I navigate to the settings page
		Then the delete group button should be displayed

	Scenario: Users cannot delete groups under which they have subscriptions
		Given I added a subscription to the group
		When I navigate to the settings page
		Then the delete group button should not be displayed

	Scenario: Deleted groups are removed from the menu and the settings page
		Given I am on the settings page
		When I click the delete group button
		Then the group should be removed from the menu
		And the group should be removed from the settings page

	Scenario: Group title should be updated on the home page
		When I delete the group
		And I navigate to the home page
		Then the deleted group should not be displayed
