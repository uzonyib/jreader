Feature: group management
		
	Scenario: Users should be able delete empty groups
		When I click the delete group button of "group #1"
		Then I should not see group "group #1" in the menu
		And I should not see group "group #1" on the settings page
		
	Scenario: Users should not be able delete non-empty groups
		Given I have created a group with name "group #1"
		And I have subscribed to "http://" under group "group #1"
		When I click the delete group button of "group #1"
		Then I should see the group in the menu
		And I should see the group on the settings page
	
	Scenario: Users should see the buttons to reorder groups
	
	Scenario: Users should be able to move a group upwards in the list
		Given I have created a group with name "group #1"
		And I have created a group with name "group #2"
		And I have created a group with name "group #3"
		When I click to move "group #2" up
		Then I should see the groups in the menu in this order: "group #2", "group #1", "group #3"
		