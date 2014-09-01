package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.UserDao;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserDaoImplTest extends AbstractDaoTest {
    
    private static final String[] NEW_USERNAMES = { "new_user_1", "new_user_2" };
    private static final String[] EXISTING_USERNAMES = { "existing_user_1", "existing_user_2" };
    
    private UserDao sut;
    
    @BeforeMethod
    public void init() {
        sut = new UserDaoImpl();
        
        for (String username : EXISTING_USERNAMES) {
            User user = new User();
            user.setUsername(username);
            sut.save(user);
        }
    }

    @Test
    public void save_IfUserIsNew_ShouldReturnUser() {
        User user = new User();
        user.setUsername(NEW_USERNAMES[0]);
        
        user = sut.save(user);
        
        assertNotNull(user);
        assertEquals(user.getUsername(), NEW_USERNAMES[0]);
    }
    
    @Test
    public void find_IfUserNotExists_ShouldReturnNull() {
        User user = sut.find(NEW_USERNAMES[0]);
        
        assertNull(user);
    }
    
    @Test
    public void find_IfUserExists_ShouldReturnUser() {
        User user = sut.find(EXISTING_USERNAMES[0]);
        
        assertNotNull(user);
        assertEquals(user.getUsername(), EXISTING_USERNAMES[0]);
    }
    
    @Test
    public void delete_IfUserExists_ShouldNotFindUser() {
        User user = new User();
        user.setUsername(EXISTING_USERNAMES[0]);
        
        sut.delete(user);
        
        assertNull(sut.find(EXISTING_USERNAMES[0]));
    }
    
    @Test
    public void saveAll_IfUsersNotExist_ShouldFindUsers() {
        List<User> users = new ArrayList<User>();
        for (String username : NEW_USERNAMES) {
            User user = new User();
            user.setUsername(username);
            users.add(user);
        }
        
        sut.saveAll(users);
        
        for (String username : NEW_USERNAMES) {
            User user = sut.find(username);
            assertNotNull(user);
            assertEquals(user.getUsername(), username);
        }
    }
    
    @Test
    public void deleteAll_IfUsersExist_ShouldNotFindUsers() {
        List<User> users = new ArrayList<User>();
        for (String username : EXISTING_USERNAMES) {
            User user = new User();
            user.setUsername(username);
            users.add(user);
        }
        
        sut.deleteAll(users);
        
        for (String username : EXISTING_USERNAMES) {
            User user = sut.find(username);
            assertNull(user);
        }
    }

}
