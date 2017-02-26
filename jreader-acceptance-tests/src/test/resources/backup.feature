Feature: Subscription settings can be backed up and restored

    Scenario: Groups and subscriptions can be exported
        Given the following feeds:
        | JZone        | Java related tutorials            |
        | SpringExpert | Everything about Spring           |
        | F1Insider    | Latest news from the world of F1  |
        | SFreviews    | Science-fiction book reviews      |
        And the following posts:
        | JZone        | Cucumber for beginners, part 1 | In this new series we will show how Cucumber can be used to...   | thomas.roberts | cucumber-part1.html   |  1h |
        | JZone        | JVM performance monitoring     | In this article I will show what tools are available...          | morris.johns   | jvm-monitoring.html   | 30s |
        | SpringExpert | Pitfalls of CDI                | Most Java based web applications use Spring as a DI framework... | jwagner        | cdi-pitfalls.html     | 10m |
        | F1Insider    | Williams unveils new F1 car    | Williams F1 team presented their brand-new...                    | f1f1           | williams-unveils.html |  2m |
        | SFreviews    | Best books of 2016             | These were the best books of last year.                          | sflover        | best-of-2016.html     |  5m |
        And a new user is logged in
        And he has the following groups:
        | Books  |
        | Java   |
        | Sports |
        And he subscribed to the following feeds:
        | Books  | SFreviews    | SFreviews       | http://localhost:8081/feeds/SFreviews/rss    |
        | Java   | JZone        | JZone           | http://localhost:8081/feeds/JZone/rss        |
        | Java   | SpringExpert | SpringExpert    | http://localhost:8081/feeds/SpringExpert/rss |
        | Sports | F1Insider    | F1Insider       | http://localhost:8081/feeds/F1Insider/rss    |
        When he navigates to the settings page
        And he clicks the export button
        Then a JSON containing his subscriptions is generated

    Scenario: Groups and subscriptions can be imported
        Given a new user is logged in
        And he is on the settings page
        When he enters the JSON
        And he clicks the import button
        And he refreshes the page
        And he navigates to the settings page
        Then the following groups and subscriptions are created for him:
        | Books  | SFreviews       |
        | Java   | JZone           |
        | Java   | SpringExpert    |
        | Sports | F1Insider       |
        And the unread count of all groups is 5
        And the unread counts of groups are:
        | Books  | 1 |
        | Java   | 3 |
        | Sports | 1 |
