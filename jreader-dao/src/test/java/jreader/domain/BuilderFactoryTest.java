package jreader.domain;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BuilderFactoryTest {
    
    private BuilderFactory factory;
    
    @BeforeMethod
    public void init() {
        this.factory = new BuilderFactory();
    }
    
    @Test
    public void createGroupBuilder_shouldReturnBuilder() {
        assertNotNull(factory.createGroupBuilder());
    }
    
    @Test
    public void createSubscriptionBuilder_shouldReturnBuilder() {
        assertNotNull(factory.createSubscriptionBuilder());
    }
    
    @Test
    public void createArchiveBuilder_shouldReturnBuilder() {
        assertNotNull(factory.createArchiveBuilder());
    }

}
