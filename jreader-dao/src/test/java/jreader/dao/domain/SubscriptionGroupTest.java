package jreader.dao.domain;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SubscriptionGroupTest extends AbstractDaoTest {
    
    private User savedUser;
    
    @BeforeMethod
    public void init() {
        User user = new User();
        user.setUsername("username");
        UserDaoImpl userDao = new UserDaoImpl();
        savedUser = userDao.save(user);
    }
    
    @Test
    public void setUser_IfUserIsNull_RefShouldBeNull() {
        SubscriptionGroup group = new SubscriptionGroup();
        
        group.setUser(null);
        
        assertNull(group.getUserRef());
        assertNull(group.getUser());
    }
    
    @Test
    public void setUser_IfUserIsNotNull_RefShouldNotBeNull() {
        SubscriptionGroup group = new SubscriptionGroup();
        
        group.setUser(savedUser);
        
        assertNotNull(group.getUserRef());
        assertNotNull(group.getUser());
    }

}
