package jreader.test.acceptance;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "classpath:user_administration.feature", strict = true)
public class UserAdministrationTest extends AbstractTest {

}
