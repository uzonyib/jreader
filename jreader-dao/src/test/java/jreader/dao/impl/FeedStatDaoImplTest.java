package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedDao;
import jreader.dao.FeedStatDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;
import jreader.domain.User;

public class FeedStatDaoImplTest extends AbstractDaoTest {
    
    private static final String USERNAME = "test_user";
    
    private static final String FEED_URL = "url";
    private static Feed savedFeed;
    
    private static final int[] COUNTS = { 1, 2, 3, 4 };
    private static final long[] REFRESH_DATES = { 100L, 110L, 120L, 130L };
    private static List<FeedStat> savedStats;
    
    private UserDao userDao;
    private FeedDao feedDao;

    private FeedStatDao sut;
    
    @BeforeMethod
    public void init() {
        userDao = new UserDaoImpl();
        feedDao = new FeedDaoImpl();
        sut = new FeedStatDaoImpl();
        
        User user = new User();
        user.setUsername(USERNAME);
        userDao.save(user);
        
        Feed feed = new Feed();
        feed.setUrl(FEED_URL);
        feed.setTitle("title");
        feed.setDescription("description");
        feed.setFeedType("feedType");
        feed.setPublishedDate(1000L);
        savedFeed = feedDao.save(feed);
        
        savedStats = new ArrayList<FeedStat>();
        for (int i = 0; i < COUNTS.length; ++ i) {
            FeedStat stat = new FeedStat();
            stat.setFeed(savedFeed);
            stat.setRefreshDate(REFRESH_DATES[i]);
            stat.setCount(COUNTS[i]);
            savedStats.add(sut.save(stat));
        }
    }
    
    @Test
    public void list_ShouldReturnAll() {
        List<FeedStat> stats = sut.list(savedFeed);
        
        assertNotNull(stats);
        assertEquals(stats.size(), 4);
        assertEquals(stats.get(0).getCount(), 1);
        assertEquals(stats.get(1).getCount(), 2);
        assertEquals(stats.get(2).getCount(), 3);
        assertEquals(stats.get(3).getCount(), 4);
    }

    @Test
    public void list_ShouldReturnMatchingStats() {
        List<FeedStat> stats = sut.list(savedFeed, 115L);
        
        assertNotNull(stats);
        assertEquals(stats.size(), 2);
        assertEquals(stats.get(0).getCount(), 3);
        assertEquals(stats.get(1).getCount(), 4);
    }

}
