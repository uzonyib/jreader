package jreader.test.util.impl;

import jreader.test.util.TestDataStoreService;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;

public class TestDataStoreServiceImpl implements TestDataStoreService {
    
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy(),
            new LocalMemcacheServiceTestConfig());
    
    private Closeable session;
    
    @Override
    public void open() {
        helper.setUp();
        session = ObjectifyService.begin();
    }

    @Override
    public void close() {
        if (session != null) {
            session.close();
        }
        helper.tearDown();
    }

}
