package jreader.test.acceptance;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "classpath:login.feature", strict = true)
public class LoginTest extends AbstractTest {

}
