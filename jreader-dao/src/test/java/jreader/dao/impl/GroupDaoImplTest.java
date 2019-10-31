package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.Group;
import jreader.domain.Role;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GroupDaoImplTest extends AbstractDaoTest {
    
    private UserDao userDao;
    
    private GroupDao sut;
    
    private List<User> users;
    private List<Group> groups;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        sut = new GroupDaoImpl();
        
        List<User> usersToBeSaved = Arrays.asList(
                new User("test_user_1", Role.USER, 1L),
                new User("test_user_2", Role.ADMIN, 2L));
        users = new ArrayList<User>();
        for (User user : usersToBeSaved) {
            users.add(userDao.save(user));
        }
        
        List<Group> groupsToBeSaved = Arrays.asList(
                new Group(users.get(0), "title_1", 10),
                new Group(users.get(0), "title_2", 5));
        groups = new ArrayList<Group>();
        for (Group group : groupsToBeSaved) {
            groups.add(sut.save(group));
        }
    }

    @Test
    public void find_IfGroupNotExists_ShouldReturnEmpty() {
        Optional<Group> actual = sut.find(users.get(0), groups.get(groups.size() - 1).getId() + 1);
        
        assertFalse(actual.isPresent());
    }
    
    @Test
    public void save_IfGroupIsNew_ShouldReturnGroup() {
        int order = 1;
        User user = users.get(order);
        String title = "new_title";
        Group group = new Group(user, title, order);

        Group actual = sut.save(group);
        
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(actual.getUser(), user);
        assertEquals(actual.getTitle(), title);
        assertEquals(actual.getOrder(), order);
    }
    
    @Test
    public void find_IfGroupExists_ShouldReturnGroup() {
        User user = users.get(0);
        
        Optional<Group> actual = sut.find(user, groups.get(0).getId());
        
        assertEquals(actual.get(), groups.get(0));
    }
    
    @Test
    public void findByTitle_IfGroupNotExists_ShouldReturnEmpty() {
        Optional<Group> actual = sut.find(users.get(0), "new_title");
        
        assertFalse(actual.isPresent());
    }
    
    @Test
    public void findByTitle_IfGroupExists_ShouldReturnGroup() {
        Optional<Group> actual = sut.find(users.get(0), groups.get(0).getTitle());
        
        assertEquals(actual.get(), groups.get(0));
    }
    
    @Test
    public void list_IfGroupsExist_ShouldReturnAllOrdered() {
        List<Group> actual = sut.list(users.get(0));
        
        assertEquals(actual, Arrays.asList(groups.get(1), groups.get(0)));
    }
    
    @Test
    public void getMaxOrder_IfThereAreNoGroups_ShouldReturnDefault() {
        int actual = sut.getMaxOrder(users.get(1));
        
        assertEquals(actual, -1);
    }
    
    @Test
    public void getMaxOrder_IfThereAreGroups_ShouldReturnMaxOrder() {
        int actual = sut.getMaxOrder(users.get(0));
        
        assertEquals(actual, groups.get(0).getOrder());
    }

}
