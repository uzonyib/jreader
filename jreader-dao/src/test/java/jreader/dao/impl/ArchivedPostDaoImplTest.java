package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedPostDao;
import jreader.dao.ArchivedPostFilter;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArchivedPostDaoImplTest extends AbstractDaoTest {
    
    private static final String USERNAME = "test_user";
    
    private static final String[] ARCHIVE_TITLES = { "archive_1", "archive_2" };
    private static final int[] ORDERS = { 10, 5 };
    private static List<Archive> SAVED_ARCHIVES;
    
    private static final String[] POST_LINKS = { "link_1", "link_2" };
    private static final String[] POST_TITLES = { "title_1", "title_2" };
    private static final String[] POST_DESCRIPTIONS = { "desc_1", "desc_2" };
    private static final String[] POST_AUTHORS = { "author_1", "author_2" };
    private static final Long[] POST_PUBLISHED_DATES = { 100L, 200L };
    private static List<ArchivedPost> SAVED_POSTS;
    
    private static final String NEW_LINK = "new_link";
    private static final String NEW_TITLE = "new_title";
    private static final String NEW_DESCRIPTION = "new_desc";
    private static final String NEW_AUTHOR = "new_author";
    private static final Long NEW_PUBLISHED_DATE = 1000L;
    
    private UserDao userDao;
    private ArchiveDao archiveDao;
    
    private ArchivedPostDao sut;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        archiveDao = new ArchiveDaoImpl();
        sut = new ArchivedPostDaoImpl();
        
        User user = new User();
        user.setUsername(USERNAME);
        userDao.save(user);
        
        SAVED_ARCHIVES = new ArrayList<Archive>();
        for (int i = 0; i < ARCHIVE_TITLES.length; ++i) {
            Archive archive = new Archive();
            archive.setUser(user);
            archive.setTitle(ARCHIVE_TITLES[i]);
            archive.setOrder(ORDERS[i]);
            SAVED_ARCHIVES.add(archiveDao.save(archive));
        }
        
        SAVED_POSTS = new ArrayList<ArchivedPost>();
        for (int i = 0; i < POST_TITLES.length; ++i) {
            ArchivedPost post = new ArchivedPost();
            post.setArchive(SAVED_ARCHIVES.get(0));
            post.setLink(POST_LINKS[i]);
            post.setTitle(POST_TITLES[i]);
            post.setDescription(POST_DESCRIPTIONS[i]);
            post.setAuthor(POST_AUTHORS[i]);
            post.setPublishDate(POST_PUBLISHED_DATES[i]);
            SAVED_POSTS.add(sut.save(post));
        }
    }

    @Test
    public void find_IfPostNotExists_ShouldReturnNull() {
        ArchivedPost post = sut.find(SAVED_ARCHIVES.get(1), 1L);
        
        assertNull(post);
    }
    
    @Test
    public void save_IfPostIsNew_ShouldReturnPost() {
        ArchivedPost post = new ArchivedPost();
        post.setArchive(SAVED_ARCHIVES.get(1));
        post.setLink(NEW_LINK);
        post.setTitle(NEW_TITLE);
        post.setDescription(NEW_DESCRIPTION);
        post.setAuthor(NEW_AUTHOR);
        post.setPublishDate(NEW_PUBLISHED_DATE);
        
        post = sut.save(post);
        
        assertNotNull(post);
        assertNotNull(post.getId());
        assertEquals(post.getArchive().getId(), SAVED_ARCHIVES.get(1).getId());
        assertEquals(post.getLink(), NEW_LINK);
        assertEquals(post.getTitle(), NEW_TITLE);
        assertEquals(post.getDescription(), NEW_DESCRIPTION);
        assertEquals(post.getAuthor(), NEW_AUTHOR);
        assertEquals(post.getPublishDate(), NEW_PUBLISHED_DATE);
    }
    
    @Test
    public void find_IfPostExists_ShouldReturnPost() {
        ArchivedPost post = sut.find(SAVED_ARCHIVES.get(0), SAVED_POSTS.get(0).getId());
        
        assertNotNull(post);
        assertEquals(post.getId(), SAVED_POSTS.get(0).getId());
        assertEquals(post.getLink(), POST_LINKS[0]);
        assertEquals(post.getTitle(), POST_TITLES[0]);
        assertEquals(post.getDescription(), POST_DESCRIPTIONS[0]);
        assertEquals(post.getAuthor(), POST_AUTHORS[0]);
        assertEquals(post.getPublishDate(), POST_PUBLISHED_DATES[0]);
    }
    
    @Test
    public void listForUser_IfOrderIsAscending_ShouldReturnAscendingList() {
        User user = new User();
        user.setUsername(USERNAME);
        
        List<ArchivedPost> posts = sut.list(user, new ArchivedPostFilter(true, 0, 10));
        
        assertNotNull(posts);
        assertEquals(posts.size(), 2);
        assertTrue(posts.get(0).getPublishDate() <= posts.get(1).getPublishDate());
    }
    
    @Test
    public void listForUser_IfOrderIsDescending_ShouldReturnDescendingList() {
        User user = new User();
        user.setUsername(USERNAME);
        
        List<ArchivedPost> posts = sut.list(user, new ArchivedPostFilter(false, 0, 10));
        
        assertNotNull(posts);
        assertEquals(posts.size(), 2);
        assertTrue(posts.get(0).getPublishDate() >= posts.get(1).getPublishDate());
    }
    
    @Test
    public void listForArchive_IfPostsNotExist_ShouldReturnEmptyList() {
        List<ArchivedPost> posts = sut.list(SAVED_ARCHIVES.get(1), new ArchivedPostFilter(true, 0, 10));
        
        assertNotNull(posts);
        assertTrue(posts.isEmpty());
    }
    
    @Test
    public void listForArchive_IfOffsetIsNotZero_ShouldReturnSubList() {
        List<ArchivedPost> posts = sut.list(SAVED_ARCHIVES.get(0), new ArchivedPostFilter(true, 1, 10));
        
        assertNotNull(posts);
        assertEquals(posts.size(), 1);
        assertEquals(posts.get(0).getId(), SAVED_POSTS.get(1).getId());
    }
    
    @Test
    public void listForArchive_IfOffsetIsOverPostCount_ShouldReturnSubList() {
        List<ArchivedPost> posts = sut.list(SAVED_ARCHIVES.get(0), new ArchivedPostFilter(true, 2, 10));
        
        assertNotNull(posts);
        assertTrue(posts.isEmpty());
    }
    
    @Test
    public void listForArchive_IfLimitIsNotZero_ShouldReturnSubList() {
        List<ArchivedPost> posts = sut.list(SAVED_ARCHIVES.get(0), new ArchivedPostFilter(true, 0, 1));
        
        assertNotNull(posts);
        assertEquals(posts.size(), 1);
        assertEquals(posts.get(0).getId(), SAVED_POSTS.get(0).getId());
    }

}
