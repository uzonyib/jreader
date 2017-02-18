package jreader.test.acceptance;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "classpath:archive_management.feature", strict = true)
public class ArchiveManagementTest extends AbstractTest {

}
