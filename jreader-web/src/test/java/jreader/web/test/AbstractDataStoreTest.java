package jreader.web.test;

import jreader.test.util.TestDataStoreService;
import jreader.test.util.impl.TestDataStoreServiceImpl;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractDataStoreTest {
    
    private TestDataStoreService dataStoreService = new TestDataStoreServiceImpl();
    
    @Before
    public void setUpHelper() {
        dataStoreService.open();
    }

    @After
    public void tearDownHelper() {
        dataStoreService.close();
    }

}
