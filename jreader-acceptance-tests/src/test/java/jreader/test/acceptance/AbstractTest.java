package jreader.test.acceptance;

import javax.annotation.PreDestroy;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.junit.Cucumber;
import jreader.test.acceptance.util.BrowserManager;

@RunWith(Cucumber.class)
abstract class AbstractTest {

    @Autowired
    private BrowserManager browserManager;

    @PreDestroy
    public void afterScenario() {
        browserManager.close();
    }

}
