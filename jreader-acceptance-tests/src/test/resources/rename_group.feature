Feature: rename subscription group

	Background:
		Given I am logged in
		And I am on the settings page
		And I have a group

	Scenario: Users can rename groups
		When I click on the title of the group
		Then the field for the new group title should be displayed
		And it should be defaulted to the original group title
		And the rename group button should be displayed
		
	Scenario: Group title should be updated in the menu and on the settings page
		Given I clicked on the title of the group
		When I enter the new title
		And I click the save button
		Then the group title should be updated in the menu
		And the group title should be updated on the settings page

	Scenario: Group title should be updated on the home page
		When I rename the group
		And I navigate to the home page
		Then the updated group title should be displayed
