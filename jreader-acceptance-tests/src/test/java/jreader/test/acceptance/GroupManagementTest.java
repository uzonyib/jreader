package jreader.test.acceptance;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "classpath:group_management.feature", strict = true)
public class GroupManagementTest extends AbstractTest {

}
