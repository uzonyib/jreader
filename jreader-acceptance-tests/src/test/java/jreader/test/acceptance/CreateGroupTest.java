package jreader.test.acceptance;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:02_create_group.feature", strict = true)
public class CreateGroupTest extends AbstractTest {

}
