package jreader.domain;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import jreader.dao.UserDao;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Group;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GroupTest extends AbstractDaoTest {
    
    private User savedUser;
    
    @BeforeMethod
    public void init() {
        User user = new User();
        user.setUsername("username");
        UserDao userDao = new UserDaoImpl();
        savedUser = userDao.save(user);
    }
    
    @Test
    public void setUser_IfUserIsNull_RefShouldBeNull() {
        Group group = new Group();
        
        group.setUser(null);
        
        assertNull(group.getUserRef());
        assertNull(group.getUser());
    }
    
    @Test
    public void setUser_IfUserIsNotNull_RefShouldNotBeNull() {
        Group group = new Group();
        
        group.setUser(savedUser);
        
        assertNotNull(group.getUserRef());
        assertNotNull(group.getUser());
    }

}
