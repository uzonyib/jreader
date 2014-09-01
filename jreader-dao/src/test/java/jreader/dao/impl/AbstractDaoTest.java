package jreader.dao.impl;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public abstract class AbstractDaoTest {
    
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig(),
            new LocalMemcacheServiceTestConfig());
    
    @BeforeMethod
    public void setUpHelper() {
        helper.setUp();
    }

    @AfterMethod
    public void tearDownHelper() {
        helper.tearDown();
    }

}
