package jreader.dao.domain;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Archive;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArchiveTest extends AbstractDaoTest {
    
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
        Archive archive = new Archive();
        
        archive.setUser(null);
        
        assertNull(archive.getUserRef());
        assertNull(archive.getUser());
    }
    
    @Test
    public void setUser_IfUserIsNotNull_RefShouldNotBeNull() {
        Archive archive = new Archive();
        
        archive.setUser(savedUser);
        
        assertNotNull(archive.getUserRef());
        assertNotNull(archive.getUser());
    }

}
