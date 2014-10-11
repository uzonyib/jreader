package jreader.domain;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import jreader.dao.UserDao;
import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.FeedDaoImpl;
import jreader.dao.impl.SubscriptionGroupDaoImpl;
import jreader.dao.impl.UserDaoImpl;
import jreader.domain.Subscription.Builder;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SubscriptionBuilderTest extends AbstractDaoTest {
    
    private static final String USERNAME = "username";
    private static final long ID = 10L;
    private static final String TITLE = "title";
    private static final int ORDER = 123;
    private static final long UPDATED_DATE = 456L;
    private static final long REFRESH_DATE = 789L;
    
    private User user;
    private SubscriptionGroup group;
    private Feed feed;
    private Builder builder;
    
    @BeforeMethod
    public void init() {
        user = new User();
        user.setUsername(USERNAME);
        UserDao userDao = new UserDaoImpl();
        user = userDao.save(user);
        
        group = new SubscriptionGroup();
        group.setId(1L);
        group.setUser(user);
        group = new SubscriptionGroupDaoImpl().save(group);
        feed = new Feed();
        feed.setUrl("url");
        feed = new FeedDaoImpl().save(feed);
        
        this.builder = new Builder();
    }
    
    @Test
    public void build_shouldReturnSubscription() {
        Subscription subscription = builder.id(ID).title(TITLE).order(ORDER)
                .updatedDate(UPDATED_DATE).refreshDate(REFRESH_DATE)
                .group(group).feed(feed).build();
        
        assertNotNull(subscription);
        assertNotNull(subscription.getId());
        assertEquals(subscription.getId().longValue(), ID);
        assertEquals(subscription.getTitle(), TITLE);
        assertEquals(subscription.getOrder(), ORDER);
        assertEquals(subscription.getUpdatedDate().longValue(), UPDATED_DATE);
        assertEquals(subscription.getRefreshDate().longValue(), REFRESH_DATE);
        assertNotNull(subscription.getGroup());
        assertNotNull(subscription.getGroup().getUser());
        assertEquals(subscription.getGroup().getUser().getUsername(), USERNAME);
        assertNotNull(subscription.getFeed());
    }

}