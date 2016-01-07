package jreader.domain;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import jreader.dao.ArchiveDao;
import jreader.dao.UserDao;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.ArchiveDaoImpl;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArchivedPostTest extends AbstractDaoTest {
    
    private User savedUser;
    private Archive savedArchive;
    
    @BeforeMethod
    public void init() {
        User user = new User();
        user.setUsername("username");
        UserDao userDao = new UserDaoImpl();
        savedUser = userDao.save(user);
        
        Archive archive = new Archive();
        archive.setUser(savedUser);
        archive.setTitle("title");
        archive.setOrder(1);
        ArchiveDao archiveDao = new ArchiveDaoImpl();
        savedArchive = archiveDao.save(archive);
    }
    
    @Test
    public void setArchive_IfArchiveIsNull_RefShouldBeNull() {
        ArchivedPost post = new ArchivedPost();
        
        post.setArchive(null);
        
        assertNull(post.getArchiveRef());
        assertNull(post.getArchive());
    }
    
    @Test
    public void setUser_IfUserIsNotNull_RefShouldNotBeNull() {
        ArchivedPost post = new ArchivedPost();
        
        post.setArchive(savedArchive);
        
        assertNotNull(post.getArchiveRef());
        assertNotNull(post.getArchive());
    }

}
