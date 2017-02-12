Feature: Group management

	Scenario: Users can create new groups
		Given a new user is logged in
		When he navigates to the settings page
		Then the group title field is displayed
		And the create group button is displayed

	Scenario: Newly created groups appear in the menu and on the settings page
		When the user enters "Java" as the group title
		And he clicks the create group button
		Then the new group is displayed in the menu
		And the new group is displayed in the subscription settings
		And the new group is displayed in the group field of the new subscription form

	Scenario: Newly created groups appear on the home page
		When the user navigates to the home page
		Then group "Java" is displayed on the home page

	Scenario: By default, groups are listed in creation order in the menu and on the settings page
		When the user navigates to the settings page
		And he creates the following groups:
		| Books  |
		| Sports |
		Then the following groups are displayed in the menu:
		| 1 | Java   |
		| 2 | Books  |
		| 3 | Sports |
		And the following groups are displayed in the subscription settings:
		| 1 | Java   |
		| 2 | Books  |
		| 3 | Sports |
		
	Scenario: By default, groups are listed in creation order on the new subscription form
		When the user opens the group dropdown
		Then the following groups are displayed in the group field of the new subscription form:
		| 1 | Java   |
		| 2 | Books  |
		| 3 | Sports |

	Scenario: By default, groups are listed in creation order on the home page
		When the user navigates to the home page
		Then the following groups are displayed on the home page:
		| 1 | Java   |
		| 2 | Books  |
		| 3 | Sports |

	Scenario: Groups can be moved down in the list
		When the user navigates to the settings page
		And he moves group "Books" down:
		Then the following groups are displayed in the subscription settings:
		| 1 | Java   |
		| 2 | Sports |
		| 3 | Books  |

	Scenario: Groups can be moved up in the list
		And the user moves group "Sports" up:
		Then the following groups are displayed in the subscription settings:
		| 1 | Sports |
		| 2 | Java   |
		| 3 | Books  |

	Scenario: Groups can be renamed
		When the user renames group "Java" to "Tech":
		Then the following groups are displayed in the subscription settings:
		| 1 | Sports |
		| 2 | Tech   |
		| 3 | Books  |

	Scenario: Groups can be renamed
		When the user deletes group "Tech":
		Then the following groups are displayed in the subscription settings:
		| 1 | Sports |
		| 2 | Books  |
