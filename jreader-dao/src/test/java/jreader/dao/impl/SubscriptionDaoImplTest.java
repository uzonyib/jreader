package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedDao;
import jreader.dao.GroupDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.Group;
import jreader.domain.Role;
import jreader.domain.Subscription;
import jreader.domain.User;

public class SubscriptionDaoImplTest extends AbstractDaoTest {
    
    private UserDao userDao;
    private FeedDao feedDao;
    private GroupDao groupDao;

    private SubscriptionDao sut;
    
    private List<User> users;
    private List<Group> groups;
    private List<Feed> feeds;
    private List<Subscription> subscriptions;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        feedDao = new FeedDaoImpl();
        groupDao = new GroupDaoImpl();
        sut = new SubscriptionDaoImpl();
        
        List<User> usersToBeSaved = Arrays.asList(
                new User("test_user_1", Role.USER, 1L),
                new User("test_user_2", Role.ADMIN, 2L));
        users = new ArrayList<User>();
        for (User user : usersToBeSaved) {
            users.add(userDao.save(user));
        }
        
        List<Feed> feedsToBeSaved = Arrays.asList(
                Feed.builder().url("url_1").title("title_1").description("desc_1").feedType("feed_type_1").lastUpdateDate(100L).lastRefreshDate(200L)
                        .status(0).build(),
                        Feed.builder().url("url_2").title("title_2").description("desc_2").feedType("feed_type_2").lastUpdateDate(300L).lastRefreshDate(400L)
                        .status(1).build());
        feeds = new ArrayList<Feed>();
        for (Feed feed : feedsToBeSaved) {
            feeds.add(feedDao.save(feed));
        }
        
        List<Group> groupsToBeSaved = Arrays.asList(
                new Group(users.get(0), "group_1", 10),
                new Group(users.get(0), "group_2", 5),
                new Group(users.get(1), "group_1", 10),
                new Group(users.get(1), "group_2", 5));
        groups = new ArrayList<Group>();
        for (Group group : groupsToBeSaved) {
            groups.add(groupDao.save(group));
        }
        
        subscriptions = new ArrayList<Subscription>();
        Subscription subscription1 = new Subscription();
        subscription1.setTitle("title_1");
        subscription1.setOrder(11);
        subscription1.setLastUpdateDate(10L);
        subscription1.setFeed(feeds.get(0));
        subscription1.setGroup(groups.get(0));
        subscriptions.add(sut.save(subscription1));
        Subscription subscription2 = new Subscription();
        subscription2.setTitle("title_2");
        subscription2.setOrder(6);
        subscription2.setLastUpdateDate(11L);
        subscription2.setFeed(feeds.get(1));
        subscription2.setGroup(groups.get(1));
        subscriptions.add(sut.save(subscription2));
    }

    @Test
    public void findForUserAndFeed_IfSubscriptionNotExists_ShouldReturnNull() {
        Subscription actual = sut.find(users.get(1), feeds.get(0));
        
        assertNull(actual);
    }
    
    @Test
    public void save_IfSubscriptionIsNew_ShouldReturnSubscription() {
        Subscription subscription = new Subscription();
        subscription.setTitle("new_title");
        subscription.setOrder(7);
        subscription.setLastUpdateDate(4L);
        subscription.setGroup(groups.get(2));
        subscription.setFeed(feeds.get(0));

        Subscription actual = sut.save(subscription);
        
        assertEquals(actual, subscription);
    }
    
    @Test
    public void findForUserAndFeed_IfSubscriptionExists_ShouldReturnSubscription() {
        Subscription actual = sut.find(users.get(0), feeds.get(0));
        
        assertEquals(actual, subscriptions.get(0));
    }
    
    @Test
    public void findForGroup_IfSubscriptionNotExists_ShouldReturnNull() {
        Subscription actual = sut.find(groups.get(2), 1L);
        
        assertNull(actual);
    }
    
    @Test
    public void findForGroup_IfSubscriptionExists_ShouldReturnSubscription() {
        Subscription subscription = sut.find(groups.get(0), subscriptions.get(0).getId());
        
        assertEquals(subscription, subscriptions.get(0));
    }
    
    @Test
    public void findForGroupAndTitle_IfSubscriptionNotExists_ShouldReturnNull() {
        Subscription actual = sut.find(groups.get(2), "title_not_found");
        
        assertNull(actual);
    }
    
    @Test
    public void findForGroupAndTitle_IfSubscriptionExists_ShouldReturnSubscription() {
        Subscription actual = sut.find(groups.get(0), subscriptions.get(0).getTitle());
        
        assertEquals(actual, subscriptions.get(0));
    }
    
    @Test
    public void listSubscriptions_IfSubscriptionsExist_ShouldReturnSubscriptions() {
        List<Subscription> actual = sut.listSubscriptions(feeds.get(0));
        
        assertEquals(actual, Arrays.asList(subscriptions.get(0)));
    }
    
    @Test
    public void list_IfSubscriptionsExist_ShouldReturnSubscriptions() {
        List<Subscription> actual = sut.list(groups.get(0));
        
        assertEquals(actual, Arrays.asList(subscriptions.get(0)));
    }
    
    @Test
    public void list_IfSubscriptionsNotExist_ShouldReturnEmpty() {
        List<Subscription> actual = sut.list(groups.get(2));
        
        assertTrue(actual.isEmpty());
    }
    
    @Test
    public void countSubscribers_IfSubscriptionsExist_ShouldReturnCount() {
        int actual = sut.countSubscribers(feeds.get(0));
        
        assertEquals(actual, 1);
    }
    
    @Test
    public void getMaxOrder_IfThereAreNoSubscriptions_ShouldReturnDefault() {
        int actual = sut.getMaxOrder(groups.get(2));
        
        assertEquals(actual, -1);
    }
    
    @Test
    public void getMaxOrder_IfThereAreSubscriptions_ShouldReturnMaxOrder() {
        int actual = sut.getMaxOrder(groups.get(0));
        
        assertEquals(actual, subscriptions.get(0).getOrder());
    }

}
