package jreader.dao.domain;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.ArchiveDaoImpl;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArchivedEntryTest extends AbstractDaoTest {
    
    private User savedUser;
    private Archive savedArchive;
    
    @BeforeMethod
    public void init() {
        User user = new User();
        user.setUsername("username");
        UserDaoImpl userDao = new UserDaoImpl();
        savedUser = userDao.save(user);
        
        Archive archive = new Archive();
        archive.setUser(savedUser);
        archive.setTitle("title");
        archive.setOrder(1);
        ArchiveDaoImpl archiveDao = new ArchiveDaoImpl();
        savedArchive = archiveDao.save(archive);
    }
    
    @Test
    public void setArchive_IfArchiveIsNull_RefShouldBeNull() {
        ArchivedEntry entry = new ArchivedEntry();
        
        entry.setArchive(null);
        
        assertNull(entry.getArchiveRef());
        assertNull(entry.getArchive());
    }
    
    @Test
    public void setUser_IfUserIsNotNull_RefShouldNotBeNull() {
        ArchivedEntry entry = new ArchivedEntry();
        
        entry.setArchive(savedArchive);
        
        assertNotNull(entry.getArchiveRef());
        assertNotNull(entry.getArchive());
    }

}
