package jreader.test.acceptance;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "classpath:post_management.feature", strict = true)
public class PostManagementTest extends AbstractTest {

}
