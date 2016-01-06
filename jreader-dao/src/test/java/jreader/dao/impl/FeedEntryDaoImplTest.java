package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.FeedEntryFilter;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.dao.FeedEntryFilter.Selection;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FeedEntryDaoImplTest extends AbstractDaoTest {
    
    private static final String USERNAME = "test_user";
    
    private static final String[] GROUP_TITLES = { "group_1", "group_2" };
    private static final int[] GROUP_ORDERS = { 10, 5 };
    private static List<SubscriptionGroup> savedGroups;
    
    private static final String[] FEED_URLS = { "url_1", "url_2" };
    private static List<Feed> savedFeeds;
    
    private static final String[] SUBSCRIPTION_TITLES = { "title_1", "title_2" };
    private static final int[] SUBSCRIPTION_ORDERS = { 11, 6 };
    private static final long[] SUBSCRIPTION_UPDATED_DATES = { 10L, 11L };
    private static List<Subscription> savedSubscriptions;
    
    private static final String[] ENTRY_URIS = { "uri_1", "uri_2", "uri_3", "uri_4" };
    private static final String[] ENTRY_LINKS = { "link_1", "link_2", "link_3", "link_4" };
    private static final String[] ENTRY_TITLES = { "title_1", "title_2", "title_3", "title_4" };
    private static final String[] ENTRY_DESCRIPTIONS = { "description_1", "description_2", "description_3", "description_4" };
    private static final String[] ENTRY_AUTHORS = { "author_1", "author_2", "author_3", "author_4" };
    private static final long[] ENTRY_PUBLISHED_DATES = { 103L, 102L, 101L, 104L };
    private static final boolean[] ENTRY_READ_FLAGS = { true, true, false, false };
    private static final boolean[] ENTRY_STARRED_FLAGS = { false, true, true, false };
    private static List<FeedEntry> savedEntries;
    
    private static final String NEW_URI = "new_uri";
    private static final String NEW_LINK = "new_link";
    private static final String NEW_TITLE = "new_title";
    private static final String NEW_DESCRIPTION = "new_description";
    private static final String NEW_AUTHOR = "new_author";
    private static final long NEW_PUBLISHED_DATE = 105L;
    private static final boolean NEW_READ_FLAG = false;
    private static final boolean NEW_STARRED_FLAG = false;
    
    private UserDao userDao;
    private FeedDao feedDao;
    private SubscriptionGroupDao groupDao;
    private SubscriptionDao subscriptionDao;

    private FeedEntryDao sut;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        feedDao = new FeedDaoImpl();
        groupDao = new SubscriptionGroupDaoImpl();
        subscriptionDao = new SubscriptionDaoImpl();
        sut = new FeedEntryDaoImpl();
        
        User user = new User();
        user.setUsername(USERNAME);
        userDao.save(user);
        
        savedFeeds = new ArrayList<Feed>();
        for (int i = 0; i < FEED_URLS.length; ++i) {
            Feed feed = new Feed();
            feed.setUrl(FEED_URLS[i]);
            feed.setTitle("title_" + i);
            feed.setDescription("description_" + i);
            feed.setFeedType("feedType_" + i);
            feed.setUpdatedDate(1000L);
            savedFeeds.add(feedDao.save(feed));
        }
        
        savedGroups = new ArrayList<SubscriptionGroup>();
        for (int i = 0; i < GROUP_TITLES.length; ++i) {
            SubscriptionGroup group = new SubscriptionGroup();
            group.setUser(user);
            group.setTitle(GROUP_TITLES[i]);
            group.setOrder(GROUP_ORDERS[i]);
            savedGroups.add(groupDao.save(group));
        }
        
        savedSubscriptions = new ArrayList<Subscription>();
        for (int i = 0; i < SUBSCRIPTION_TITLES.length; ++ i) {
            Subscription subscription = new Subscription();
            subscription.setTitle(SUBSCRIPTION_TITLES[i]);
            subscription.setOrder(SUBSCRIPTION_ORDERS[i]);
            subscription.setUpdatedDate(SUBSCRIPTION_UPDATED_DATES[i]);
            subscription.setFeed(savedFeeds.get(i));
            subscription.setGroup(savedGroups.get(0));
            savedSubscriptions.add(subscriptionDao.save(subscription));
        }
        
        savedEntries = new ArrayList<FeedEntry>();
        for (int i = 0; i < ENTRY_TITLES.length; ++ i) {
            FeedEntry entry = new FeedEntry();
            entry.setUri(ENTRY_URIS[i]);
            entry.setLink(ENTRY_LINKS[i]);
            entry.setTitle(ENTRY_TITLES[i]);
            entry.setDescription(ENTRY_DESCRIPTIONS[i]);
            entry.setAuthor(ENTRY_AUTHORS[i]);
            entry.setLink(ENTRY_LINKS[i]);
            entry.setPublishedDate(ENTRY_PUBLISHED_DATES[i]);
            entry.setRead(ENTRY_READ_FLAGS[i]);
            entry.setStarred(ENTRY_STARRED_FLAGS[i]);
            entry.setSubscription(savedSubscriptions.get(0));
            savedEntries.add(sut.save(entry));
        }
    }

    @Test
    public void findById_IfEntryNotExists_ShouldReturnNull() {
        FeedEntry entry = sut.find(savedSubscriptions.get(1), 1L);
        
        assertNull(entry);
    }
    
    @Test
    public void save_IfSubscriptionIsNew_ShouldReturnSubscription() {
        FeedEntry entry = new FeedEntry();
        entry.setUri(NEW_URI);
        entry.setLink(NEW_LINK);
        entry.setTitle(NEW_TITLE);
        entry.setDescription(NEW_DESCRIPTION);
        entry.setAuthor(NEW_AUTHOR);
        entry.setPublishedDate(NEW_PUBLISHED_DATE);
        entry.setRead(NEW_READ_FLAG);
        entry.setStarred(NEW_STARRED_FLAG);
        entry.setSubscription(savedSubscriptions.get(1));

        entry = sut.save(entry);
        
        assertNotNull(entry);
        assertNotNull(entry.getId());
        assertEquals(entry.getSubscription().getTitle(), SUBSCRIPTION_TITLES[1]);
        assertEquals(entry.getUri(), NEW_URI);
        assertEquals(entry.getLink(), NEW_LINK);
        assertEquals(entry.getTitle(), NEW_TITLE);
        assertEquals(entry.getDescription(), NEW_DESCRIPTION);
        assertEquals(entry.getAuthor(), NEW_AUTHOR);
        assertEquals(entry.getPublishedDate().longValue(), NEW_PUBLISHED_DATE);
        assertEquals(entry.isRead(), NEW_READ_FLAG);
        assertEquals(entry.isStarred(), NEW_STARRED_FLAG);
    }
    
    @Test
    public void findByUri_IfEntryNotExists_ShouldReturnNull() {
        FeedEntry entry = sut.find(savedSubscriptions.get(1), "not_found", 1L);
        
        assertNull(entry);
    }
    
    @Test
    public void findByUri_IfEntryExists_ShouldReturnEntry() {
        FeedEntry entry = sut.find(savedSubscriptions.get(0), savedEntries.get(0).getUri(), savedEntries.get(0).getPublishedDate());
        
        assertNotNull(entry);
        assertNotNull(entry.getId());
        assertEquals(entry.getSubscription().getTitle(), SUBSCRIPTION_TITLES[0]);
        assertEquals(entry.getUri(), ENTRY_URIS[0]);
        assertEquals(entry.getLink(), ENTRY_LINKS[0]);
        assertEquals(entry.getTitle(), ENTRY_TITLES[0]);
        assertEquals(entry.getDescription(), ENTRY_DESCRIPTIONS[0]);
        assertEquals(entry.getAuthor(), ENTRY_AUTHORS[0]);
        assertEquals(entry.getPublishedDate().longValue(), ENTRY_PUBLISHED_DATES[0]);
        assertEquals(entry.isRead(), ENTRY_READ_FLAGS[0]);
        assertEquals(entry.isStarred(), ENTRY_STARRED_FLAGS[0]);
    }
    
    @Test
    public void findById_IfEntryExists_ShouldReturnEntry() {
        FeedEntry entry = sut.find(savedSubscriptions.get(0), savedEntries.get(0).getId());
        
        assertNotNull(entry);
        assertNotNull(entry.getId());
        assertEquals(entry.getSubscription().getTitle(), SUBSCRIPTION_TITLES[0]);
        assertEquals(entry.getUri(), ENTRY_URIS[0]);
        assertEquals(entry.getLink(), ENTRY_LINKS[0]);
        assertEquals(entry.getTitle(), ENTRY_TITLES[0]);
        assertEquals(entry.getDescription(), ENTRY_DESCRIPTIONS[0]);
        assertEquals(entry.getAuthor(), ENTRY_AUTHORS[0]);
        assertEquals(entry.getPublishedDate().longValue(), ENTRY_PUBLISHED_DATES[0]);
        assertEquals(entry.isRead(), ENTRY_READ_FLAGS[0]);
        assertEquals(entry.isStarred(), ENTRY_STARRED_FLAGS[0]);
    }
    
    @Test
    public void findByOrdinal_IfEntryExists_ShouldReturnEntry() {
        FeedEntry entry = sut.find(savedSubscriptions.get(0), 2);
        
        assertNotNull(entry);
        assertNotNull(entry.getId());
        assertEquals(entry.getId(), savedEntries.get(0).getId());
    }
    
    @Test
    public void listForUser_IfEntriesExist_ShouldReturnEntries() {
        User user = new User();
        user.setUsername(USERNAME);
        FeedEntryFilter filter = new FeedEntryFilter(Selection.ALL, true, 1, 2);
        
        List<FeedEntry> entries = sut.list(user, filter);
        
        assertNotNull(entries);
        assertEquals(entries.size(), 2);
        assertEquals(entries.get(0).getId(), savedEntries.get(1).getId());
        assertEquals(entries.get(1).getId(), savedEntries.get(0).getId());
        
    }
    
    @Test
    public void listForGroup_IfEntriesExist_ShouldReturnEntries() {
        FeedEntryFilter filter = new FeedEntryFilter(Selection.UNREAD, false, 0, 5);
        
        List<FeedEntry> entries = sut.list(savedGroups.get(0), filter);
        
        assertNotNull(entries);
        assertEquals(entries.size(), 2);
        assertEquals(entries.get(0).getId(), savedEntries.get(3).getId());
        assertEquals(entries.get(1).getId(), savedEntries.get(2).getId());
        
    }
    
    @Test
    public void listForSubscription_IfEntriesExist_ShouldReturnEntries() {
        FeedEntryFilter filter = new FeedEntryFilter(Selection.STARRED, true, 0, 5);
        
        List<FeedEntry> entries = sut.list(savedSubscriptions.get(0), filter);
        
        assertNotNull(entries);
        assertEquals(entries.size(), 2);
        assertEquals(entries.get(0).getId(), savedEntries.get(2).getId());
        assertEquals(entries.get(1).getId(), savedEntries.get(1).getId());
        
    }
    
    @Test
    public void listUnstarredOlderThan_IfEntriesExist_ShouldReturnEntries() {
        List<FeedEntry> entries = sut.listUnstarredOlderThan(savedSubscriptions.get(0), 104);
        
        assertNotNull(entries);
        assertEquals(entries.size(), 1);
        assertEquals(entries.get(0).getId(), savedEntries.get(0).getId());
        
    }
    
    @Test
    public void list_IfEntriesExist_ShouldReturnEntries() {
        List<FeedEntry> entries = sut.list(savedSubscriptions.get(0));
        
        assertNotNull(entries);
        assertEquals(entries.size(), 4);
    }
    
    @Test
    public void countUnread_IfEntriesExist_ShouldReturnCount() {
        int count = sut.countUnread(savedSubscriptions.get(0));
        
        assertEquals(count, 2);
    }

}
