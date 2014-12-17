package jreader.dao.impl;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

public abstract class AbstractDaoTest {
    
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(1.0f),
            new LocalMemcacheServiceTestConfig());
    
    private Closeable session;
    
    @BeforeMethod
    public void setUpHelper() {
        helper.setUp();
        session = ObjectifyService.begin();
    }

    @AfterMethod
    public void tearDownHelper() {
        session.close();
        helper.tearDown();
    }

}
