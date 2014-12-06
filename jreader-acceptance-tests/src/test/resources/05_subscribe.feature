Feature: subscribe to feed

	Background:
		Given I am logged in
		And I have multiple groups

	Scenario: Users can subscribe to feeds
		When I navigate to the settings page
		Then the group field should be displayed
		And the group field options should hold all group titles
		And by default the first group is selected

	Scenario: Newly created subscriptions appear in the menu and on the settings page
		When I navigate to the settings page
		And I select the group
		And I enter the feed URL
		And I click the subscribe button
		Then the number of unread items is displayed next to the group title in the menu
		And the default title of the new subscription should be displayed in the subscription settings page under the selected group

	Scenario: Newly created subscriptions appear on the home page
		Given I subscribed to a feed
		When I navigate to the home page
		Then the default title of the new subscription should be displayed under the correct group
		And the feed link is displayed
		And the feed description is displayed
		And the refresh date is displayed
		And the update date is displayed
		And the unread count is displayed

#	Scenario: By default, subscriptions are listed in creation order on the settings page
#		When I subscribe to multiple feeds under the same group
#		Then the default titles of the new subscriptions should be displayed in the subscription settings under the correct group in creation order

#	Scenario: By default, subscriptions are listed in creation order on the home page
#		Given I subscribed to multiple feeds under the same group
#		When I navigate to the home page
#		Then the default titles of the new subscriptions should be displayed under the correct group in creation order

#	Scenario: Next to the group title in the menu, the sum of unread item count of the group's subscriptions is displayed
#		When I subscribe to multiple feeds under the same group
#		Then I should see the number of unread items next to the group title in the menu
		
#	Scenario: Subscriptions are listed ordered in the menu along with the number of their unread items
#		Given I subscribed to multiple feeds under the same group
#		When I expand the group in the menu
#		Then I should see the default subscriptions titles in the menu under the group
#		And I should see the number of unread items of each subscription
