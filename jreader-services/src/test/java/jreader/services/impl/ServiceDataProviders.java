package jreader.services.impl;

import org.testng.annotations.DataProvider;

public class ServiceDataProviders {

    @DataProvider(name = "invalidUsernames")
    private String[] getInvalidUsernames() {
        return new String[] { null, "" };
    }

    @DataProvider(name = "invalidRoles")
    private String[] getInvalidRoles() {
        return new String[] { null };
    }

    @DataProvider(name = "invalidGroupTitles")
    private String[] getInvalidGroupTitles() {
        return new String[] { null, "" };
    }

    @DataProvider(name = "invalidArchiveTitles")
    private String[] getInvalidArchiveTitles() {
        return new String[] { null, "" };
    }

}
