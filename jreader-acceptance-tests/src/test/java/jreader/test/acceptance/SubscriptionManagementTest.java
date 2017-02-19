package jreader.test.acceptance;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "classpath:subscription_management.feature", strict = true)
public class SubscriptionManagementTest extends AbstractTest {

}
