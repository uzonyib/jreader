package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;

public class UserDaoImplTest extends AbstractDaoTest {
    
    private UserDao sut;
    
    private List<User> users;
    
    @BeforeMethod
    public void init() {
        sut = new UserDaoImpl();
        
        List<User> usersToBeSaved = Arrays.asList(
                new User("existing_user_1", Role.ADMIN, 1L),
                new User("existing_user_2", Role.USER, 2L),
                new User("existing_user_4", Role.UNAUTHORIZED, 3L),
                new User("existing_user_3", Role.UNAUTHORIZED, 4L));
        users = new ArrayList<User>();
        for (User user : usersToBeSaved) {
            users.add(sut.save(user));
        }
    }

    @Test
    public void save_IfUserIsNew_ShouldReturnUser() {
        User user = new User("new_user", Role.USER, 10L);
        
        User actual = sut.save(user);
        
        assertEquals(actual, user);
    }
    
    @Test
    public void find_IfUserNotExists_ShouldReturnNull() {
        User actual = sut.find("new_user");
        
        assertNull(actual);
    }
    
    @Test
    public void find_IfUserExists_ShouldReturnUser() {
        User actual = sut.find(users.get(0).getUsername());
        
        assertEquals(actual, users.get(0));
    }
    
    @Test
    public void delete_IfUserExists_ShouldNotFindUser() {
        User user = users.get(0);
        
        sut.delete(user);
        
        assertNull(sut.find(user.getUsername()));
    }
    
    @Test
    public void saveAll_IfUsersNotExist_ShouldFindUsers() {
        List<User> newUsers = Arrays.asList(
                new User("new_user_1", Role.ADMIN, 5L),
                new User("new_user_2", Role.USER, 6L));
        
        sut.saveAll(newUsers);
        
        for (User user : newUsers) {
            assertEquals(sut.find(user.getUsername()), user);
        }
    }
    
    @Test
    public void deleteAll_IfUsersExist_ShouldNotFindUsers() {
        sut.deleteAll(users);
        
        for (User user : users) {
            assertNull(sut.find(user.getUsername()));
        }
    }
    
    @Test
    public void list_ShouldReturnUsersOrdered() {
        List<User> actual = sut.list(0, 10);
        
        assertEquals(actual, Arrays.asList(users.get(0), users.get(1), users.get(3), users.get(2)));
    }
    
    @Test
    public void list_ShouldReturnUsersLimited() {
        List<User> actual = sut.list(1, 2);
        
        assertEquals(actual, Arrays.asList(users.get(1), users.get(3)));
    }

}
