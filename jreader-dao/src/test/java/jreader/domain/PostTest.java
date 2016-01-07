package jreader.domain;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import jreader.dao.FeedDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.FeedDaoImpl;
import jreader.dao.impl.SubscriptionDaoImpl;
import jreader.dao.impl.GroupDaoImpl;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Feed;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PostTest extends AbstractDaoTest {
    
    private User savedUser;
    private Group savedGroup;
    private Feed savedFeed;
    private Subscription savedSubscription;
    
    @BeforeMethod
    public void init() {
        User user = new User();
        user.setUsername("username");
        UserDao userDao = new UserDaoImpl();
        savedUser = userDao.save(user);
        
        Group group = new Group();
        group.setUser(savedUser);
        group.setTitle("title");
        group.setOrder(1);
        GroupDao groupDao = new GroupDaoImpl();
        savedGroup = groupDao.save(group);
        
        Feed feed = new Feed();
        feed.setTitle("title");
        feed.setDescription("description");
        feed.setUrl("url");
        feed.setFeedType("feedType");
        FeedDao feedDao = new FeedDaoImpl();
        savedFeed = feedDao.save(feed);
        
        Subscription subscription = new Subscription();
        subscription.setFeed(savedFeed);
        subscription.setGroup(savedGroup);
        subscription.setTitle("title");
        subscription.setOrder(1);
        subscription.setLastUpdateDate(2000L);
        SubscriptionDao subscriptionDao = new SubscriptionDaoImpl();
        savedSubscription = subscriptionDao.save(subscription);
    }
    
    @Test
    public void setSubscription_IfSubscriptionIsNull_RefShouldBeNull() {
        Post post = new Post();
        
        post.setSubscription(null);
        
        assertNull(post.getSubscriptionRef());
        assertNull(post.getSubscription());
    }
    
    @Test
    public void setSubscription_IfSubscriptionIsNotNull_RefShouldNotBeNull() {
        Post post = new Post();
        
        post.setSubscription(savedSubscription);
        
        assertNotNull(post.getSubscriptionRef());
        assertNotNull(post.getSubscription());
    }

}
