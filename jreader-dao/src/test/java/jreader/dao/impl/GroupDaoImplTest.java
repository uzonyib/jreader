package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.Group;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GroupDaoImplTest extends AbstractDaoTest {
    
    private static final String[] USERNAMES = { "test_user_1", "test_user_2" };
    private static final String[] TITLES = { "title_1", "title_2" };
    private static final int[] ORDERS = { 10, 5 };
    private static List<Group> SAVED_GROUPS;
    private static final String NEW_TITLE = "new_title";
    private static final int NEW_ORDER = 1;
    
    private UserDao userDao;
    
    private GroupDao sut;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        sut = new GroupDaoImpl();
        
        for (String username : USERNAMES) {
            User user = new User();
            user.setUsername(username);
            userDao.save(user);
        }
        
        SAVED_GROUPS = new ArrayList<Group>();
        User user = new User();
        user.setUsername(USERNAMES[0]);
        for (int i = 0; i < TITLES.length; ++i) {
            Group group = new Group();
            group.setUser(user);
            group.setTitle(TITLES[i]);
            group.setOrder(ORDERS[i]);
            SAVED_GROUPS.add(sut.save(group));
        }
    }

    @Test
    public void find_IfGroupNotExists_ShouldReturnNull() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Group group = sut.find(user, SAVED_GROUPS.get(SAVED_GROUPS.size() - 1).getId() + 1);
        
        assertNull(group);
    }
    
    @Test
    public void save_IfGroupIsNew_ShouldReturnGroup() {
        User user = new User();
        user.setUsername(USERNAMES[1]);
        Group group = new Group();
        group.setUser(user);
        group.setTitle(NEW_TITLE);
        group.setOrder(NEW_ORDER);

        group = sut.save(group);
        
        assertNotNull(group);
        assertNotNull(group.getId());
        assertEquals(group.getUser().getUsername(), USERNAMES[1]);
        assertEquals(group.getTitle(), NEW_TITLE);
        assertEquals(group.getOrder(), NEW_ORDER);
    }
    
    @Test
    public void find_IfGroupExists_ShouldReturnGroup() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Group group = sut.find(user, SAVED_GROUPS.get(0).getId());
        
        assertNotNull(group);
        assertEquals(group.getId(), SAVED_GROUPS.get(0).getId());
        assertEquals(group.getTitle(), TITLES[0]);
        assertEquals(group.getOrder(), ORDERS[0]);
        assertEquals(group.getUser().getUsername(), USERNAMES[0]);
    }
    
    @Test
    public void findByTitle_IfGroupNotExists_ShouldReturnNull() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Group group = sut.find(user, NEW_TITLE);
        
        assertNull(group);
    }
    
    @Test
    public void findByTitle_IfGroupExists_ShouldReturnGroup() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Group group = sut.find(user, TITLES[0]);
        
        assertNotNull(group);
        assertEquals(group.getId(), SAVED_GROUPS.get(0).getId());
        assertEquals(group.getTitle(), TITLES[0]);
        assertEquals(group.getOrder(), ORDERS[0]);
        assertEquals(group.getUser().getUsername(), USERNAMES[0]);
    }
    
    @Test
    public void list_IfGroupsExist_ShouldReturnAllOrdered() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        List<Group> groups = sut.list(user);
        
        assertNotNull(groups);
        assertEquals(groups.size(), 2);
        assertEquals(groups.get(0).getTitle(), TITLES[1]);
        assertEquals(groups.get(1).getTitle(), TITLES[0]);
    }
    
    @Test
    public void getMaxOrder_IfThereAreNoGroups_ShouldReturnDefault() {
        User user = new User();
        user.setUsername(USERNAMES[1]);
        
        int maxOrder = sut.getMaxOrder(user);
        
        assertEquals(maxOrder, -1);
    }
    
    @Test
    public void getMaxOrder_IfThereAreGroups_ShouldReturnMaxOrder() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        int maxOrder = sut.getMaxOrder(user);
        
        assertEquals(maxOrder, ORDERS[0]);
    }

}
