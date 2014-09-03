package jreader.dao.domain;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import jreader.dao.FeedDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.FeedDaoImpl;
import jreader.dao.impl.SubscriptionDaoImpl;
import jreader.dao.impl.SubscriptionGroupDaoImpl;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FeedEntryTest extends AbstractDaoTest {
    
    private User savedUser;
    private SubscriptionGroup savedGroup;
    private Feed savedFeed;
    private Subscription savedSubscription;
    
    @BeforeMethod
    public void init() {
        User user = new User();
        user.setUsername("username");
        UserDao userDao = new UserDaoImpl();
        savedUser = userDao.save(user);
        
        SubscriptionGroup group = new SubscriptionGroup();
        group.setUser(savedUser);
        group.setTitle("title");
        group.setOrder(1);
        SubscriptionGroupDao groupDao = new SubscriptionGroupDaoImpl();
        savedGroup = groupDao.save(group);
        
        Feed feed = new Feed();
        feed.setTitle("title");
        feed.setDescription("description");
        feed.setUrl("url");
        feed.setFeedType("feedType");
        feed.setPublishedDate(1000L);
        FeedDao feedDao = new FeedDaoImpl();
        savedFeed = feedDao.save(feed);
        
        Subscription subscription = new Subscription();
        subscription.setFeed(savedFeed);
        subscription.setGroup(savedGroup);
        subscription.setTitle("title");
        subscription.setOrder(1);
        subscription.setRefreshDate(1000L);
        subscription.setUpdatedDate(2000L);
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl();
        savedSubscription = subscriptionDao.save(subscription);
    }
    
    @Test
    public void setSubscription_IfSubscriptionIsNull_RefShouldBeNull() {
        FeedEntry entry = new FeedEntry();
        
        entry.setSubscription(null);
        
        assertNull(entry.getSubscriptionRef());
        assertNull(entry.getSubscription());
    }
    
    @Test
    public void setSubscription_IfSubscriptionIsNotNull_RefShouldNotBeNull() {
        FeedEntry entry = new FeedEntry();
        
        entry.setSubscription(savedSubscription);
        
        assertNotNull(entry.getSubscriptionRef());
        assertNotNull(entry.getSubscription());
    }

}
