package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedPostDao;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.Role;
import jreader.domain.User;

public class ArchivedPostDaoImplTest extends AbstractDaoTest {

    private static final String SORT_PROPERTY = "publishDate";

    private UserDao userDao;
    private ArchiveDao archiveDao;
    
    private ArchivedPostDao sut;
    
    private User user;
    private static List<Archive> archives;
    private static List<ArchivedPost> posts;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        archiveDao = new ArchiveDaoImpl();
        sut = new ArchivedPostDaoImpl();
        
        user = userDao.save(new User("test_user", Role.USER, 100L));
        
        List<Archive> archiveToBeSaved = Arrays.asList(
                new Archive(user, "archive_1", 10),
                new Archive(user, "archive_2", 5));
        archives = new ArrayList<Archive>();
        for (Archive archive : archiveToBeSaved) {
            archives.add(archiveDao.save(archive));
        }
        
        posts = new ArrayList<ArchivedPost>();
        for (int i = 0; i < 2; ++i) {
            ArchivedPost post = new ArchivedPost();
            post.setArchive(archives.get(0));
            post.setLink("link_" + i);
            post.setTitle("title_" + i);
            post.setDescription("description_" + i);
            post.setAuthor("author_" + i);
            post.setPublishDate(100L * i);
            posts.add(sut.save(post));
        }
    }

    @Test
    public void find_IfPostNotExists_ShouldReturnEmpty() {
        Optional<ArchivedPost> actual = sut.find(archives.get(1), 1L);
        
        assertFalse(actual.isPresent());
    }
    
    @Test
    public void save_IfPostIsNew_ShouldReturnPost() {
        ArchivedPost post = new ArchivedPost();
        post.setArchive(archives.get(1));
        post.setLink("new_link");
        post.setTitle("new_title");
        post.setDescription("new_description");
        post.setAuthor("new_author");
        post.setPublishDate(1000L);
        
        ArchivedPost actual = sut.save(post);
        
        assertEquals(actual, post);
    }
    
    @Test
    public void find_IfPostExists_ShouldReturnPost() {
        Optional<ArchivedPost> actual = sut.find(archives.get(0), posts.get(0).getId());
        
        assertEquals(actual.get(), posts.get(0));
    }
    
    @Test
    public void listForUser_IfOrderIsAscending_ShouldReturnAscendingList() {
        PageRequest page = new PageRequest(0, 10, new Sort(Direction.ASC, SORT_PROPERTY));

        List<ArchivedPost> actual = sut.list(user, page);

        assertEquals(actual, posts);
    }
    
    @Test
    public void listForUser_IfOrderIsDescending_ShouldReturnDescendingList() {
        PageRequest page = new PageRequest(0, 10, new Sort(Direction.DESC, SORT_PROPERTY));

        List<ArchivedPost> actual = sut.list(user, page);

        assertEquals(actual, Arrays.asList(posts.get(1), posts.get(0)));
    }
    
    @Test
    public void listForArchiveAndFilter_IfPostsNotExist_ShouldReturnEmptyList() {
        PageRequest page = new PageRequest(0, 10, new Sort(Direction.ASC, SORT_PROPERTY));

        List<ArchivedPost> actual = sut.list(archives.get(1), page);

        assertTrue(actual.isEmpty());
    }
    
    @Test
    public void listForArchiveAndFilter_IfOffsetIsNotZero_ShouldReturnSubList() {
        PageRequest page = new PageRequest(1, 1, new Sort(Direction.ASC, SORT_PROPERTY));

        List<ArchivedPost> actual = sut.list(archives.get(0), page);

        assertEquals(actual, Arrays.asList(posts.get(1)));
    }
    
    @Test
    public void listForArchiveAndFilter_IfOffsetIsOverPostCount_ShouldReturnSubList() {
        PageRequest page = new PageRequest(1, 2, new Sort(Direction.ASC, SORT_PROPERTY));

        List<ArchivedPost> actual = sut.list(archives.get(0), page);

        assertTrue(actual.isEmpty());
    }
    
    @Test
    public void listForArchiveAndFilter_IfLimitIsNotZero_ShouldReturnSubList() {
        PageRequest page = new PageRequest(0, 1, new Sort(Direction.ASC, SORT_PROPERTY));

        List<ArchivedPost> actual = sut.list(archives.get(0), page);

        assertEquals(actual, Arrays.asList(posts.get(0)));
    }
    
    @Test
    public void listForArchive_IfPostsNotExist_ShouldReturnEmptyList() {
        List<ArchivedPost> actual = sut.list(archives.get(1));
        
        assertTrue(actual.isEmpty());
    }
    
    @Test
    public void listForArchive_IfPostsExist_ShouldReturnAll() {
        List<ArchivedPost> actual = sut.list(archives.get(0));
        
        assertEquals(actual, Arrays.asList(posts.get(0), posts.get(1)));
    }

}
