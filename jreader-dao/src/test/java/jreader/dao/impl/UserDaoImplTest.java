package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class UserDaoImplTest extends AbstractDaoTest {
    
    private static final String[] NEW_USERNAMES = { "new_user_1", "new_user_2" };
    private static final Role[] NEW_ROLES = { Role.ADMIN, Role.USER };
    private static final Long[] NEW_REGISTRATION_DATES = { 5L, 6L };
    private static final String[] EXISTING_USERNAMES = { "existing_user_1", "existing_user_2", "existing_user_4", "existing_user_3" };
    private static final Role[] EXISTING_ROLES = { Role.ADMIN, Role.USER, Role.UNAUTHORIZED, Role.UNAUTHORIZED };
    private static final Long[] EXISTING_REGISTRATION_DATES = { 1L, 2L, 3L, 4L };
    
    private UserDao sut;
    
    @BeforeMethod
    public void init() {
        sut = new UserDaoImpl();
        
        for (int i = 0; i < EXISTING_USERNAMES.length; ++i) {
            User user = new User();
            user.setUsername(EXISTING_USERNAMES[i]);
            user.setRole(EXISTING_ROLES[i]);
            user.setRegistrationDate(EXISTING_REGISTRATION_DATES[i]);
            sut.save(user);
        }
    }

    @Test
    public void save_IfUserIsNew_ShouldReturnUser() {
        User user = new User();
        user.setUsername(NEW_USERNAMES[0]);
        user.setRole(NEW_ROLES[0]);
        user.setRegistrationDate(NEW_REGISTRATION_DATES[0]);
        
        user = sut.save(user);
        
        assertNotNull(user);
        assertEquals(user.getUsername(), NEW_USERNAMES[0]);
        assertEquals(user.getRole(), NEW_ROLES[0]);
        assertEquals(user.getRegistrationDate(), NEW_REGISTRATION_DATES[0]);
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
        assertEquals(user.getRole(), EXISTING_ROLES[0]);
        assertEquals(user.getRegistrationDate(), EXISTING_REGISTRATION_DATES[0]);
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
        for (int i = 0; i < NEW_USERNAMES.length; ++i) {
            User user = new User();
            user.setUsername(NEW_USERNAMES[i]);
            user.setRole(NEW_ROLES[i]);
            user.setRegistrationDate(NEW_REGISTRATION_DATES[i]);
            users.add(user);
        }
        
        sut.saveAll(users);
        
        for (int i = 0; i < NEW_USERNAMES.length; ++i) {
            User user = sut.find(NEW_USERNAMES[i]);
            assertNotNull(user);
            assertEquals(user.getUsername(), NEW_USERNAMES[i]);
            assertEquals(user.getRole(), NEW_ROLES[i]);
            assertEquals(user.getRegistrationDate(), NEW_REGISTRATION_DATES[i]);
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
    
    @Test
    public void list_ShouldReturnUsersOrdered() {
        List<User> users = sut.list(0, 10);
        
        assertNotNull(users);
        assertEquals(users.size(), 4);
        assertEquals(users.get(0).getUsername(), EXISTING_USERNAMES[0]);
        assertEquals(users.get(0).getRole(), EXISTING_ROLES[0]);
        assertEquals(users.get(0).getRegistrationDate(), EXISTING_REGISTRATION_DATES[0]);
        assertEquals(users.get(1).getUsername(), EXISTING_USERNAMES[1]);
        assertEquals(users.get(1).getRole(), EXISTING_ROLES[1]);
        assertEquals(users.get(1).getRegistrationDate(), EXISTING_REGISTRATION_DATES[1]);
        assertEquals(users.get(2).getUsername(), EXISTING_USERNAMES[3]);
        assertEquals(users.get(2).getRole(), EXISTING_ROLES[3]);
        assertEquals(users.get(2).getRegistrationDate(), EXISTING_REGISTRATION_DATES[3]);
        assertEquals(users.get(3).getUsername(), EXISTING_USERNAMES[2]);
        assertEquals(users.get(3).getRole(), EXISTING_ROLES[2]);
        assertEquals(users.get(3).getRegistrationDate(), EXISTING_REGISTRATION_DATES[2]);
    }
    
    @Test
    public void list_ShouldReturnUsersLimited() {
        List<User> users = sut.list(1, 2);
        
        assertNotNull(users);
        assertEquals(users.size(), 2);
        assertEquals(users.get(0).getUsername(), EXISTING_USERNAMES[1]);
        assertEquals(users.get(0).getRole(), EXISTING_ROLES[1]);
        assertEquals(users.get(0).getRegistrationDate(), EXISTING_REGISTRATION_DATES[1]);
        assertEquals(users.get(1).getUsername(), EXISTING_USERNAMES[3]);
        assertEquals(users.get(1).getRole(), EXISTING_ROLES[3]);
        assertEquals(users.get(1).getRegistrationDate(), EXISTING_REGISTRATION_DATES[3]);
    }

}
