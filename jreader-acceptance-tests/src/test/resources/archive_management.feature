Feature: Archive management

    Scenario: Users can create new archives
        Given a new user is logged in
        When he navigates to the settings page
        Then the archive title field is displayed
        And the create archive button is displayed

    Scenario: Newly created archives appear on the settings page
        When the user enters "Video tutorials" as the archive title
        And he clicks the create archive button
        Then the new archive is displayed in the archive settings

    Scenario: Newly created archives appear in the menu
        When the user expands the archives menu item
        Then the archive "Video tutorials" is displayed in the menu

    Scenario: By default, archives are listed in creation order in the menu and on the settings page
        When the user navigates to the settings page
        And he creates the following archives:
        | Interview questions |
        | Book reviews        |
        Then the following archives are displayed in the menu:
        | 1 | Video tutorials      |
        | 2 | Interview questions  |
        | 3 | Book reviews         |
        And the following archives are displayed in the archive settings:
        | 1 | Video tutorials      |
        | 2 | Interview questions  |
        | 3 | Book reviews         |

    Scenario: Archives can be moved down in the list
        When the user navigates to the settings page
        And he moves archive "Interview questions" down:
        Then the following archives are displayed in the archive settings:
        | 1 | Video tutorials      |
        | 2 | Book reviews         |
        | 3 | Interview questions  |

    Scenario: Archives can be moved up in the list
        And the user moves archive "Book reviews" up:
        Then the following archives are displayed in the archive settings:
        | 1 | Book reviews         |
        | 2 | Video tutorials      |
        | 3 | Interview questions  |

    Scenario: Archives can be renamed
        When the user renames archive "Book reviews" to "Book critiques":
        Then the following archives are displayed in the archive settings:
        | 1 | Book critiques       |
        | 2 | Video tutorials      |
        | 3 | Interview questions  |

    Scenario: Archives can be deleted
        When the user deletes archive "Video tutorials":
        Then the following archives are displayed in the archive settings:
        | 1 | Book critiques       |
        | 2 | Interview questions  |
