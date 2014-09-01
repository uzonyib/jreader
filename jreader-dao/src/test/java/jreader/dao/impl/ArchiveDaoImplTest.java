package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.ArchiveDao;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArchiveDaoImplTest extends AbstractDaoTest {
    
    private static final String[] USERNAMES = { "test_user_1", "test_user_2" };
    private static final String[] TITLES = { "title_1", "title_2" };
    private static final int[] ORDERS = { 10, 5 };
    private static List<Archive> SAVED_ARCHIVES;
    private static final String NEW_TITLE = "new_title";
    private static final int NEW_ORDER = 1;
    
    private UserDao userDao;
    
    private ArchiveDao sut;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        sut = new ArchiveDaoImpl();
        
        for (String username : USERNAMES) {
            User user = new User();
            user.setUsername(username);
            userDao.save(user);
        }
        
        SAVED_ARCHIVES = new ArrayList<Archive>();
        User user = new User();
        user.setUsername(USERNAMES[0]);
        for (int i = 0; i < TITLES.length; ++i) {
            Archive archive = new Archive();
            archive.setUser(user);
            archive.setTitle(TITLES[i]);
            archive.setOrder(ORDERS[i]);
            SAVED_ARCHIVES.add(sut.save(archive));
        }
    }

    @Test
    public void find_IfArchiveNotExists_ShouldReturnNull() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Archive archive = sut.find(user, SAVED_ARCHIVES.get(SAVED_ARCHIVES.size() - 1).getId() + 1);
        
        assertNull(archive);
    }
    
    @Test
    public void save_IfArchiveIsNew_ShouldReturnArchive() {
        User user = new User();
        user.setUsername(USERNAMES[1]);
        Archive archive = new Archive();
        archive.setUser(user);
        archive.setTitle(NEW_TITLE);
        archive.setOrder(NEW_ORDER);

        archive = sut.save(archive);
        
        assertNotNull(archive);
        assertNotNull(archive.getId());
        assertEquals(archive.getUser().getUsername(), USERNAMES[1]);
        assertEquals(archive.getTitle(), NEW_TITLE);
        assertEquals(archive.getOrder(), NEW_ORDER);
    }
    
    @Test
    public void find_IfArchiveExists_ShouldReturnArchive() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Archive archive = sut.find(user, SAVED_ARCHIVES.get(0).getId());
        
        assertNotNull(archive);
        assertEquals(archive.getId(), SAVED_ARCHIVES.get(0).getId());
        assertEquals(archive.getTitle(), TITLES[0]);
        assertEquals(archive.getOrder(), ORDERS[0]);
        assertEquals(archive.getUser().getUsername(), USERNAMES[0]);
    }
    
    @Test
    public void findByTitle_IfArchiveNotExists_ShouldReturnNull() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Archive archive = sut.find(user, NEW_TITLE);
        
        assertNull(archive);
    }
    
    @Test
    public void findByTitle_IfArchiveExists_ShouldReturnArchive() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Archive archive = sut.find(user, TITLES[0]);
        
        assertNotNull(archive);
        assertEquals(archive.getId(), SAVED_ARCHIVES.get(0).getId());
        assertEquals(archive.getTitle(), TITLES[0]);
        assertEquals(archive.getOrder(), ORDERS[0]);
        assertEquals(archive.getUser().getUsername(), USERNAMES[0]);
    }
    
    @Test
    public void list_IfArchivesExist_ShouldReturnAllOrdered() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        List<Archive> archives = sut.list(user);
        
        assertNotNull(archives);
        assertEquals(archives.size(), 2);
        assertEquals(archives.get(0).getTitle(), TITLES[1]);
        assertEquals(archives.get(1).getTitle(), TITLES[0]);
    }
    
    @Test
    public void getMaxOrder_IfThereAreNoArchives_ShouldReturnDefault() {
        User user = new User();
        user.setUsername(USERNAMES[1]);
        
        int maxOrder = sut.getMaxOrder(user);
        
        assertEquals(maxOrder, -1);
    }
    
    @Test
    public void getMaxOrder_IfThereAreArchives_ShouldReturnMaxOrder() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        int maxOrder = sut.getMaxOrder(user);
        
        assertEquals(maxOrder, ORDERS[0]);
    }

}
