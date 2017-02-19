Feature: Subscription management

    Scenario: Users can subscribe to feeds
        Given a new user is logged in
        And he is on the settings page
        And he has the following groups:
        | Books  |
        | Java   |
        | Sports |
        Then the subscription URL field should be displayed
        And the group field dropdown should be displayed

    Scenario: Newly created subscriptions appear in the menu and on the settings page
        Given the following feeds:
        | J-Tech       | News related to Java technologies |
        | JZone        | Java related tutorials            |
        | SpringExpert | Everything about Spring           |
        When the user navigates to the settings page
        And he enters "http://localhost:8081/feeds/J-Tech/rss" as the feed URL
        And he selects "Java" as the group
        And he clicks the subscribe button
        Then the subscription "J-Tech" is displayed in the subscription settings under group "Java"

    Scenario: Newly created subscriptions appear in the menu
        When the user expands the "Java" group menu item
        Then the subscription "J-Tech" is displayed in the menu

    Scenario: Newly created subscriptions appear on the home page
        When the user navigates to the home page
        Then subscription "J-Tech" is displayed on the home page

    Scenario: By default, subscriptions are listed in creation order in the menu and on the settings page
        When the user navigates to the settings page
        And he subscribes to the following feeds under group "Java":
        | http://localhost:8081/feeds/JZone/rss        |
        | http://localhost:8081/feeds/SpringExpert/rss |
        Then the following subscriptions are displayed in the menu:
        | J-Tech       |
        | JZone        |
        | SpringExpert |
        And the following subscriptions are displayed in the subscription settings under group "Java":
        | J-Tech       |
        | JZone        |
        | SpringExpert |

    Scenario: Subscriptions can be moved down in the list
        When he moves subscription "JZone" of group "Java" down:
        Then the following subscriptions are displayed in the subscription settings under group "Java":
        | J-Tech       |
        | SpringExpert |
        | JZone        |

    Scenario: Subscriptions can be moved up in the list
        And the user moves subscription "SpringExpert" of group "Java" up:
        Then the following subscriptions are displayed in the subscription settings under group "Java":
        | SpringExpert |
        | J-Tech       |
        | JZone        |

    Scenario: Subscriptions can be renamed
        When the user renames subscription "SpringExpert" of group "Java" to "SE":
        Then the following subscriptions are displayed in the subscription settings under group "Java":
        | SE     |
        | J-Tech |
        | JZone  |

    Scenario: Subscriptions can be deleted
        When the user unsubscribes from "J-Tech" of group "Java":
        Then the following subscriptions are displayed in the subscription settings under group "Java":
        | SE    |
        | JZone |
