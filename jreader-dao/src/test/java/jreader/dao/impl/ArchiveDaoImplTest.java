package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.ArchiveDao;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.Role;
import jreader.domain.User;

public class ArchiveDaoImplTest extends AbstractDaoTest {
    
    private UserDao userDao;
    
    private ArchiveDao sut;
    
    private List<User> users;
    private List<Archive> archives;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        sut = new ArchiveDaoImpl();
        
        List<User> usersToBeSaved = Arrays.asList(
                new User("test_user_1", Role.USER, 1L),
                new User("test_user_2", Role.ADMIN, 2L));
        users = new ArrayList<User>();
        for (User user : usersToBeSaved) {
            users.add(userDao.save(user));
        }
        
        List<Archive> archiveToBeSaved = Arrays.asList(
                new Archive(users.get(0), "title_1", 10),
                new Archive(users.get(0), "title_2", 5));
        archives = new ArrayList<Archive>();
        for (Archive archive : archiveToBeSaved) {
            archives.add(sut.save(archive));
        }
    }

    @Test
    public void find_IfArchiveNotExists_ShouldReturnEmpty() {
        Optional<Archive> actual = sut.find(users.get(0), archives.get(archives.size() - 1).getId() + 1);
        
        assertFalse(actual.isPresent());
    }
    
    @Test
    public void save_IfArchiveIsNew_ShouldReturnArchive() {
        User user = users.get(0);
        Archive archive = new Archive(user, "new_title", 1);

        Archive actual = sut.save(archive);
        
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(actual.getUser(), user);
        assertEquals(actual.getTitle(), archive.getTitle());
        assertEquals(actual.getOrder(), archive.getOrder());
    }
    
    @Test
    public void find_IfArchiveExists_ShouldReturnArchive() {
        Optional<Archive> actual = sut.find(users.get(0), archives.get(0).getId());
        
        assertEquals(actual.get(), archives.get(0));
    }
    
    @Test
    public void findByTitle_IfArchiveNotExists_ShouldReturnEmpty() {
        Optional<Archive> actual = sut.find(users.get(0), "new_title");
        
        assertFalse(actual.isPresent());
    }
    
    @Test
    public void findByTitle_IfArchiveExists_ShouldReturnArchive() {
        Optional<Archive> actual = sut.find(users.get(0), archives.get(0).getTitle());
        
        assertEquals(actual.get(), archives.get(0));
    }
    
    @Test
    public void list_IfArchivesExist_ShouldReturnAllOrdered() {
        List<Archive> actual = sut.list(users.get(0));
        
        assertEquals(actual, Arrays.asList(archives.get(1), archives.get(0)));
    }
    
    @Test
    public void getMaxOrder_IfThereAreNoArchives_ShouldReturnDefault() {
        int actual = sut.getMaxOrder(users.get(1));
        
        assertEquals(actual, -1);
    }
    
    @Test
    public void getMaxOrder_IfThereAreArchives_ShouldReturnMaxOrder() {
        int actual = sut.getMaxOrder(users.get(0));
        
        assertEquals(actual, archives.get(0).getOrder());
    }

}
