package jreader.domain;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import jreader.dao.FeedDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.FeedDaoImpl;
import jreader.dao.impl.GroupDaoImpl;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SubscriptionTest extends AbstractDaoTest {
    
    private User savedUser;
    private Group savedGroup;
    private Feed savedFeed;
    
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
    }
    
    @Test
    public void setGroup_IfGroupIsNull_RefShouldBeNull() {
        Subscription subscription = new Subscription();
        
        subscription.setGroup(null);
        
        assertNull(subscription.getGroupRef());
        assertNull(subscription.getGroup());
    }
    
    @Test
    public void setFeed_IfFeedIsNull_RefShouldBeNull() {
        Subscription subscription = new Subscription();
        
        subscription.setFeed(null);
        
        assertNull(subscription.getFeedRef());
        assertNull(subscription.getFeed());
    }
    
    @Test
    public void setGroup_IfGroupIsNotNull_RefShouldNotBeNull() {
        Subscription subscription = new Subscription();
        
        subscription.setGroup(savedGroup);
        
        assertNotNull(subscription.getGroupRef());
        assertNotNull(subscription.getGroup());
    }
    
    @Test
    public void setFeed_IfFeedIsNotNull_RefShouldNotBeNull() {
        Subscription subscription = new Subscription();
        
        subscription.setFeed(savedFeed);
        
        assertNotNull(subscription.getFeedRef());
        assertNotNull(subscription.getFeed());
    }

}
