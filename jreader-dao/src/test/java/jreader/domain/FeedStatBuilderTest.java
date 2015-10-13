package jreader.domain;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.impl.AbstractDaoTest;
import jreader.dao.impl.FeedDaoImpl;
import jreader.domain.FeedStat.Builder;

public class FeedStatBuilderTest extends AbstractDaoTest {
    
    private static final String URL = "url";
    private static final long REFRESH_DATE = 789L;
    private static final int COUNT = 6;
    
    private Feed feed;
    private Builder builder;
    
    @BeforeMethod
    public void init() {
        feed = new Feed();
        feed.setUrl(URL);
        feed = new FeedDaoImpl().save(feed);
        
        this.builder = new Builder();
    }
    
    @Test
    public void build_shouldReturnFeedStat() {
        FeedStat feedStat = builder.feed(feed).refreshDate(REFRESH_DATE).count(COUNT).build();
        
        assertNotNull(feedStat);
        assertNull(feedStat.getId());
        assertNotNull(feedStat.getFeed());
        assertEquals(feedStat.getFeed().getUrl(), URL);
        assertEquals(feedStat.getRefreshDate().longValue(), REFRESH_DATE);
        assertEquals(feedStat.getCount(), COUNT);
    }

}
