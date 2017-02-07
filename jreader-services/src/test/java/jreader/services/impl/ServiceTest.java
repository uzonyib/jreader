package jreader.services.impl;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;

import com.googlecode.objectify.Ref;

@PrepareForTest(Ref.class)
public class ServiceTest extends PowerMockTestCase {

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new PowerMockObjectFactory();
    }

    @BeforeMethod()
    public void setupMockingForRef() {
        PowerMockito.mockStatic(Ref.class);
    }

}
