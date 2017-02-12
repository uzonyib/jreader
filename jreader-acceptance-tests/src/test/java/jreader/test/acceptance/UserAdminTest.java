package jreader.test.acceptance;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "classpath:user_admin.feature", strict = true)
public class UserAdminTest extends AbstractTest {

}
