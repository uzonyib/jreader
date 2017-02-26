package jreader.test.acceptance;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "classpath:backup.feature", strict = true)
public class BackupTest extends AbstractTest {

}
