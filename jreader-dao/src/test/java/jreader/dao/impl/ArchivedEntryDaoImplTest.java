package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.ArchiveDao;
import jreader.dao.ArchivedEntryDao;
import jreader.dao.ArchivedEntryFilter;
import jreader.dao.UserDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ArchivedEntryDaoImplTest extends AbstractDaoTest {
    
    private static final String USERNAME = "test_user";
    
    private static final String[] ARCHIVE_TITLES = { "archive_1", "archive_2" };
    private static final int[] ORDERS = { 10, 5 };
    private static List<Archive> SAVED_ARCHIVES;
    
    private static final String[] ENTRY_LINKS = { "link_1", "link_2" };
    private static final String[] ENTRY_TITLES = { "title_1", "title_2" };
    private static final String[] ENTRY_DESCRIPTIONS = { "desc_1", "desc_2" };
    private static final String[] ENTRY_AUTHORS = { "author_1", "author_2" };
    private static final Long[] ENTRY_PUBLISHED_DATES = { 100L, 200L };
    private static List<ArchivedEntry> SAVED_ENTRIES;
    
    private static final String NEW_LINK = "new_link";
    private static final String NEW_TITLE = "new_title";
    private static final String NEW_DESCRIPTION = "new_desc";
    private static final String NEW_AUTHOR = "new_author";
    private static final Long NEW_PUBLISHED_DATE = 1000L;
    
    private UserDao userDao;
    private ArchiveDao archiveDao;
    
    private ArchivedEntryDao sut;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        archiveDao = new ArchiveDaoImpl();
        sut = new ArchivedEntryDaoImpl();
        
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
        
        SAVED_ENTRIES = new ArrayList<ArchivedEntry>();
        for (int i = 0; i < ENTRY_TITLES.length; ++i) {
            ArchivedEntry entry = new ArchivedEntry();
            entry.setArchive(SAVED_ARCHIVES.get(0));
            entry.setLink(ENTRY_LINKS[i]);
            entry.setTitle(ENTRY_TITLES[i]);
            entry.setDescription(ENTRY_DESCRIPTIONS[i]);
            entry.setAuthor(ENTRY_AUTHORS[i]);
            entry.setPublishedDate(ENTRY_PUBLISHED_DATES[i]);
            SAVED_ENTRIES.add(sut.save(entry));
        }
    }

    @Test
    public void find_IfEntryNotExists_ShouldReturnNull() {
        ArchivedEntry entry = sut.find(SAVED_ARCHIVES.get(1), 1L);
        
        assertNull(entry);
    }
    
    @Test
    public void save_IfEntryIsNew_ShouldReturnEntry() {
        ArchivedEntry entry = new ArchivedEntry();
        entry.setArchive(SAVED_ARCHIVES.get(1));
        entry.setLink(NEW_LINK);
        entry.setTitle(NEW_TITLE);
        entry.setDescription(NEW_DESCRIPTION);
        entry.setAuthor(NEW_AUTHOR);
        entry.setPublishedDate(NEW_PUBLISHED_DATE);
        
        entry = sut.save(entry);
        
        assertNotNull(entry);
        assertNotNull(entry.getId());
        assertEquals(entry.getArchive().getId(), SAVED_ARCHIVES.get(1).getId());
        assertEquals(entry.getLink(), NEW_LINK);
        assertEquals(entry.getTitle(), NEW_TITLE);
        assertEquals(entry.getDescription(), NEW_DESCRIPTION);
        assertEquals(entry.getAuthor(), NEW_AUTHOR);
        assertEquals(entry.getPublishedDate(), NEW_PUBLISHED_DATE);
    }
    
    @Test
    public void find_IfEntryExists_ShouldReturnEntry() {
        ArchivedEntry entry = sut.find(SAVED_ARCHIVES.get(0), SAVED_ENTRIES.get(0).getId());
        
        assertNotNull(entry);
        assertEquals(entry.getId(), SAVED_ENTRIES.get(0).getId());
        assertEquals(entry.getLink(), ENTRY_LINKS[0]);
        assertEquals(entry.getTitle(), ENTRY_TITLES[0]);
        assertEquals(entry.getDescription(), ENTRY_DESCRIPTIONS[0]);
        assertEquals(entry.getAuthor(), ENTRY_AUTHORS[0]);
        assertEquals(entry.getPublishedDate(), ENTRY_PUBLISHED_DATES[0]);
    }
    
    @Test
    public void listForUser_IfOrderIsAscending_ShouldReturnAscendingList() {
        User user = new User();
        user.setUsername(USERNAME);
        
        List<ArchivedEntry> entries = sut.list(user, new ArchivedEntryFilter(true, 0, 10));
        
        assertNotNull(entries);
        assertEquals(entries.size(), 2);
        assertTrue(entries.get(0).getPublishedDate() <= entries.get(1).getPublishedDate());
    }
    
    @Test
    public void listForUser_IfOrderIsDescending_ShouldReturnDescendingList() {
        User user = new User();
        user.setUsername(USERNAME);
        
        List<ArchivedEntry> entries = sut.list(user, new ArchivedEntryFilter(false, 0, 10));
        
        assertNotNull(entries);
        assertEquals(entries.size(), 2);
        assertTrue(entries.get(0).getPublishedDate() >= entries.get(1).getPublishedDate());
    }
    
    @Test
    public void listForArchive_IfEntriesNotExist_ShouldReturnEmptyList() {
        List<ArchivedEntry> entries = sut.list(SAVED_ARCHIVES.get(1), new ArchivedEntryFilter(true, 0, 10));
        
        assertNotNull(entries);
        assertTrue(entries.isEmpty());
    }
    
    @Test
    public void listForArchive_IfOffsetIsNotZero_ShouldReturnSubList() {
        List<ArchivedEntry> entries = sut.list(SAVED_ARCHIVES.get(0), new ArchivedEntryFilter(true, 1, 10));
        
        assertNotNull(entries);
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getId(), SAVED_ENTRIES.get(1).getId());
    }
    
    @Test
    public void listForArchive_IfOffsetIsOverEntryCount_ShouldReturnSubList() {
        List<ArchivedEntry> entries = sut.list(SAVED_ARCHIVES.get(0), new ArchivedEntryFilter(true, 2, 10));
        
        assertNotNull(entries);
        assertTrue(entries.isEmpty());
    }
    
    @Test
    public void listForArchive_IfLimitIsNotZero_ShouldReturnSubList() {
        List<ArchivedEntry> entries = sut.list(SAVED_ARCHIVES.get(0), new ArchivedEntryFilter(true, 0, 1));
        
        assertNotNull(entries);
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getId(), SAVED_ENTRIES.get(0).getId());
    }

}
