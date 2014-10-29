Feature: create subscription group

	Background:
		Given I am logged in as an admin user

	Scenario: Users should see the form for creating groups on the settings page
		When I navigate to the settings page
		Then I should see a form for creating a new group

	Scenario: Users should be able to create new groups
		When I navigate to the settings page
		And I enter "group 1" as the group title
		And I click the create group button
		Then I should see the new group in the menu
		And I should see the new group on the settings page
		And I should see the new group on the home page

	Scenario: Groups should be listed in the order of their creation by default
		When I create groups "group 1,group 2,group 3"
		Then I should see the groups in the menu in creation order
		And I should see the groups on the settings page in creation order
		And I should see the groups on the home page in creation order
		
