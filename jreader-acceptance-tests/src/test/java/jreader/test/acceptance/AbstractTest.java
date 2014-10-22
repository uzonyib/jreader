package jreader.test.acceptance;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class AbstractTest {
    
    @BeforeClass
    public static void beforeScenario() {
        WebDriverManager.init();
    }
    
    @AfterClass
    public static void afterScenario() {
        WebDriverManager.close();
    }

}
