Feature: Post management

    Scenario: Posts of feeds the user subscribed to are displayed on the all items page
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
        | Books  | SFreviews    | SFR   | http://localhost:8081/feeds/SFreviews/rss    |
        | Java   | JZone        | JZone | http://localhost:8081/feeds/JZone/rss        |
        | Java   | SpringExpert | SE    | http://localhost:8081/feeds/SpringExpert/rss |
        | Sports | F1Insider    | F1    | http://localhost:8081/feeds/F1Insider/rss    |
        When he navigates to the all items page
        Then he can see the following posts:
        | JZone | Cucumber for beginners, part 1 |
        | SE    | Pitfalls of CDI                |
        | SFR   | Best books of 2016             |
        | F1    | Williams unveils new F1 car    |
        | JZone | JVM performance monitoring     |
        And the unread count of all groups is 5
        And the unread counts of groups are:
        | Books  | 1 |
        | Java   | 3 |
        | Sports | 1 |

    Scenario: Unread counts of subscriptions are displayed in the menu
        When the user expands the "Java" group menu item
        Then the unread counts of subscriptions are:
        | JZone  | 2 |
        | SE     | 1 |

    Scenario: Posts of a single group can be displayed
        When the user clicks group "Java" in the menu
        Then he can see the following posts:
        | JZone | Cucumber for beginners, part 1 |
        | SE    | Pitfalls of CDI                |
        | JZone | JVM performance monitoring     |

    Scenario: Posts of a single subscription can be displayed
        When he clicks subscription "JZone" in the menu
        Then he can see the following posts:
        | JZone | Cucumber for beginners, part 1 |
        | JZone | JVM performance monitoring     |

    Scenario: Post details can be opened
        Given the user is viewing all unread posts
        When the user clicks post title "Best books of 2016"
    	Then post description "These were the best books of last year." is displayed
    	And author "sflover" is displayed
    	And a link pointing to "http://localhost:8081/feeds/SFreviews/rss/best-of-2016.html" is displayed

	Scenario: Read posts are not shown in the unread posts view
	    When the user clicks the refresh posts button
		Then he can see the following posts:
        | JZone | Cucumber for beginners, part 1 |
        | SE    | Pitfalls of CDI                |
        | F1    | Williams unveils new F1 car    |
        | JZone | JVM performance monitoring     |

	Scenario: Posts can be bookmarked
	    When the user bookmarks the following posts:
	    | Cucumber for beginners, part 1 |
	    | JVM performance monitoring     |
	    | Pitfalls of CDI                |
		And he opens the bookmarked posts view
		Then he can see the following posts:
        | JZone | Cucumber for beginners, part 1 |
        | SE    | Pitfalls of CDI                |
        | JZone | JVM performance monitoring     |

	Scenario: Bookmarks can be deleted
	    When the user deletes bookmark of post "Cucumber for beginners, part 1"
		And he clicks the refresh posts button
		Then he can see the following posts:
        | SE    | Pitfalls of CDI                |
        | JZone | JVM performance monitoring     |

    Scenario: Posts can be marked as read in bulk
    	Given the user is on the unread posts view
    	When he clicks the mark all posts read button
    	Then no posts are displayed after the view refreshes

    Scenario: All posts view displays all posts including read ones
        When the user opens the all posts view
        Then he can see the following posts:
        | JZone | Cucumber for beginners, part 1 |
        | SE    | Pitfalls of CDI                |
        | SFR   | Best books of 2016             |
        | F1    | Williams unveils new F1 car    |
        | JZone | JVM performance monitoring     |

    Scenario: Posts can be displayed in descending order
        When the user clicks the descending order button
        Then he can see the following posts:
        | JZone | JVM performance monitoring     |
        | F1    | Williams unveils new F1 car    |
        | SFR   | Best books of 2016             |
        | SE    | Pitfalls of CDI                |
        | JZone | Cucumber for beginners, part 1 |

    Scenario: Posts can be archived
        Given the user has the following archives:
        | Whishlist |
        | Tutorials |
        And he is viewing all posts
        And he opened the details of "Cucumber for beginners, part 1"
        When he selects archive "Tutorials"
        And clicks the archive button
        Then the message "Archived to Tutorials" is displayed

    Scenario: Archived posts can be displayed on the archives page
        Given the user archived post "Best books of 2016" to "Whishlist"
        When he opens the archives view
        Then he can see the following archived posts:
        | Cucumber for beginners, part 1 |
        | Best books of 2016             |

    Scenario: Archived posts of a single archive can be displayed
        Given the user expanded the archives menu item
        When he clicks archive "Tutorials" in the menu
        Then he can see the following archived posts:
        | Cucumber for beginners, part 1 |

    Scenario: Details of archived posts can be displayed
        Given the user is viewing all archived posts
        When the user clicks archived post title "Best books of 2016"
    	Then archived post description "These were the best books of last year." is displayed
    	And archived post author "sflover" is displayed
    	And an archived post link pointing to "http://localhost:8081/feeds/SFreviews/rss/best-of-2016.html" is displayed

    Scenario: Archived posts can be removed
        When clicks the delete post button of "Best books of 2016"
        Then he can see the following archived posts:
        | Cucumber for beginners, part 1 |
