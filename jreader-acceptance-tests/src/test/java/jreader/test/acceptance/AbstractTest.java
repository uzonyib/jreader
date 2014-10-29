package jreader.test.acceptance;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AbstractTest {
    
    @BeforeClass
    public static void beforeScenario() {
        BrowserManager.init();
    }
    
    @AfterClass
    public static void afterScenario() {
        BrowserManager.close();
    }

}
