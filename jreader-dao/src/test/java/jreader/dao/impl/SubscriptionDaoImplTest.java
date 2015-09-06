package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.FeedDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SubscriptionDaoImplTest extends AbstractDaoTest {
    
    private static final String[] USERNAMES = { "test_user_1", "test_user_2" };
    
    private static final String[] GROUP_TITLES = { "group_1", "group_2" };
    private static final int[] GROUP_ORDERS = { 10, 5 };
    private static List<SubscriptionGroup> savedGroups;
    
    private static final String[] FEED_URLS = { "url_1", "url_2" };
    private static List<Feed> savedFeeds;
    
    private static final String[] SUBSCRIPTION_TITLES = { "title_1", "title_2" };
    private static final int[] SUBSCRIPTION_ORDERS = { 11, 6 };
    private static final long[] SUBSCRIPTION_UPDATED_DATES = { 10L, 11L };
    private static final long[] SUBSCRIPTION_REFRESH_DATES = { 12L, 13L };
    private static List<Subscription> savedSubscriptions;
    
    private static final String NEW_TITLE = "new_title";
    private static final int NEW_ORDER = 7;
    private static final long NEW_UPDATED_DATE = 4L;
    private static final long NEW_REFRESH_DATE = 5L;
    
    private UserDao userDao;
    private FeedDao feedDao;
    private SubscriptionGroupDao groupDao;

    private SubscriptionDao sut;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        feedDao = new FeedDaoImpl();
        groupDao = new SubscriptionGroupDaoImpl();
        sut = new SubscriptionDaoImpl();
        
        for (String username : USERNAMES) {
            User user = new User();
            user.setUsername(username);
            userDao.save(user);
        }
        
        savedFeeds = new ArrayList<Feed>();
        for (int i = 0; i < FEED_URLS.length; ++i) {
            Feed feed = new Feed();
            feed.setUrl(FEED_URLS[i]);
            feed.setTitle("title_" + i);
            feed.setDescription("description_" + i);
            feed.setFeedType("feedType_" + i);
            feed.setPublishedDate(1000L);
            savedFeeds.add(feedDao.save(feed));
        }
        
        savedGroups = new ArrayList<SubscriptionGroup>();
        for (int i = 0; i < USERNAMES.length; ++i) {
            User user = new User();
            user.setUsername(USERNAMES[i]);
            for (int j = 0; j < GROUP_TITLES.length; ++j) {
                SubscriptionGroup group = new SubscriptionGroup();
                group.setUser(user);
                group.setTitle(GROUP_TITLES[j]);
                group.setOrder(GROUP_ORDERS[j]);
                savedGroups.add(groupDao.save(group));
            }
        }
        
        savedSubscriptions = new ArrayList<Subscription>();
        for (int i = 0; i < SUBSCRIPTION_TITLES.length; ++ i) {
            Subscription subscription = new Subscription();
            subscription.setTitle(SUBSCRIPTION_TITLES[i]);
            subscription.setOrder(SUBSCRIPTION_ORDERS[i]);
            subscription.setUpdatedDate(SUBSCRIPTION_UPDATED_DATES[i]);
            subscription.setRefreshDate(SUBSCRIPTION_REFRESH_DATES[i]);
            subscription.setFeed(savedFeeds.get(i));
            subscription.setGroup(savedGroups.get(i));
            savedSubscriptions.add(sut.save(subscription));
        }
    }

    @Test
    public void findForUserAndFeed_IfSubscriptionNotExists_ShouldReturnNull() {
        User user = new User();
        user.setUsername(USERNAMES[1]);
        
        Subscription subscription = sut.find(user, savedFeeds.get(0));
        
        assertNull(subscription);
    }
    
    @Test
    public void save_IfSubscriptionIsNew_ShouldReturnSubscription() {
        Subscription subscription = new Subscription();
        subscription.setTitle(NEW_TITLE);
        subscription.setOrder(NEW_ORDER);
        subscription.setUpdatedDate(NEW_UPDATED_DATE);
        subscription.setRefreshDate(NEW_REFRESH_DATE);
        subscription.setGroup(savedGroups.get(2));
        subscription.setFeed(savedFeeds.get(0));

        subscription = sut.save(subscription);
        
        assertNotNull(subscription);
        assertNotNull(subscription.getId());
        assertEquals(subscription.getGroup().getTitle(), GROUP_TITLES[0]);
        assertEquals(subscription.getGroup().getUser().getUsername(), USERNAMES[1]);
        assertEquals(subscription.getTitle(), NEW_TITLE);
        assertEquals(subscription.getOrder(), NEW_ORDER);
        assertEquals(subscription.getUpdatedDate().longValue(), NEW_UPDATED_DATE);
        assertEquals(subscription.getRefreshDate().longValue(), NEW_REFRESH_DATE);
    }
    
    @Test
    public void findForUserAndFeed_IfSubscriptionExists_ShouldReturnSubscription() {
        User user = new User();
        user.setUsername(USERNAMES[0]);
        
        Subscription subscription = sut.find(user, savedFeeds.get(0));
        
        assertNotNull(subscription);
        assertEquals(subscription.getId(), savedSubscriptions.get(0).getId());
        assertEquals(subscription.getTitle(), SUBSCRIPTION_TITLES[0]);
        assertEquals(subscription.getOrder(), SUBSCRIPTION_ORDERS[0]);
        assertEquals(subscription.getGroup().getTitle(), GROUP_TITLES[0]);
        assertEquals(subscription.getGroup().getUser().getUsername(), USERNAMES[0]);
    }
    
    @Test
    public void findForGroup_IfSubscriptionNotExists_ShouldReturnNull() {
        Subscription subscription = sut.find(savedGroups.get(2), 1L);
        
        assertNull(subscription);
    }
    
    @Test
    public void findForGroup_IfSubscriptionExists_ShouldReturnSubscription() {
        Subscription subscription = sut.find(savedGroups.get(0), savedSubscriptions.get(0).getId());
        
        assertNotNull(subscription);
        assertEquals(subscription.getId(), savedSubscriptions.get(0).getId());
        assertEquals(subscription.getTitle(), SUBSCRIPTION_TITLES[0]);
        assertEquals(subscription.getOrder(), SUBSCRIPTION_ORDERS[0]);
        assertEquals(subscription.getGroup().getTitle(), GROUP_TITLES[0]);
        assertEquals(subscription.getGroup().getUser().getUsername(), USERNAMES[0]);
    }
    
    @Test
    public void findForGroupAndTitle_IfSubscriptionNotExists_ShouldReturnNull() {
        Subscription subscription = sut.find(savedGroups.get(2), "title_not_found");
        
        assertNull(subscription);
    }
    
    @Test
    public void findForGroupAndTitle_IfSubscriptionExists_ShouldReturnSubscription() {
        Subscription subscription = sut.find(savedGroups.get(0), savedSubscriptions.get(0).getTitle());
        
        assertNotNull(subscription);
        assertEquals(subscription.getId(), savedSubscriptions.get(0).getId());
        assertEquals(subscription.getTitle(), SUBSCRIPTION_TITLES[0]);
        assertEquals(subscription.getOrder(), SUBSCRIPTION_ORDERS[0]);
        assertEquals(subscription.getGroup().getTitle(), GROUP_TITLES[0]);
        assertEquals(subscription.getGroup().getUser().getUsername(), USERNAMES[0]);
    }
    
    @Test
    public void listSubscriptions_IfSubscriptionsExist_ShouldReturnSubscriptions() {
        List<Subscription> subscriptions = sut.listSubscriptions(savedFeeds.get(0));
        
        assertNotNull(subscriptions);
        assertEquals(subscriptions.size(), 1);
        assertEquals(subscriptions.get(0).getTitle(), SUBSCRIPTION_TITLES[0]);
    }
    
    @Test
    public void list_IfSubscriptionsExist_ShouldReturnSubscriptions() {
        List<Subscription> subscriptions = sut.list(savedGroups.get(0));
        
        assertNotNull(subscriptions);
        assertEquals(subscriptions.size(), 1);
        assertEquals(subscriptions.get(0).getTitle(), SUBSCRIPTION_TITLES[0]);
    }
    
    @Test
    public void list_IfSubscriptionsNotExist_ShouldReturnEmpty() {
        List<Subscription> subscriptions = sut.list(savedGroups.get(2));
        
        assertNotNull(subscriptions);
        assertTrue(subscriptions.isEmpty());
    }
    
    @Test
    public void countSubscribers_IfSubscriptionsExist_ShouldReturnCount() {
        int count = sut.countSubscribers(savedFeeds.get(0));
        
        assertEquals(count, 1);
    }
    
    @Test
    public void getMaxOrder_IfThereAreNoSubscriptions_ShouldReturnDefault() {
        int maxOrder = sut.getMaxOrder(savedGroups.get(2));
        
        assertEquals(maxOrder, -1);
    }
    
    @Test
    public void getMaxOrder_IfThereAreSubscriptions_ShouldReturnMaxOrder() {
        int maxOrder = sut.getMaxOrder(savedGroups.get(0));
        
        assertEquals(maxOrder, SUBSCRIPTION_ORDERS[0]);
    }

}
