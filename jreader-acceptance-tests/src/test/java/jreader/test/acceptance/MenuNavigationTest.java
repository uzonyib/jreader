package jreader.test.acceptance;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:02_menu_navigation.feature", strict = true)
public class MenuNavigationTest extends AbstractTest {

}
