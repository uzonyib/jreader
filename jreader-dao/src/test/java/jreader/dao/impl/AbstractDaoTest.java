package jreader.dao.impl;

import jreader.test.util.TestDataStoreService;
import jreader.test.util.impl.TestDataStoreServiceImpl;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractDaoTest {
    
    private TestDataStoreService dataStoreService = new TestDataStoreServiceImpl();
    
    @BeforeMethod
    public void setUpHelper() {
        dataStoreService.open();
    }

    @AfterMethod
    public void tearDownHelper() {
        dataStoreService.close();
    }

}
