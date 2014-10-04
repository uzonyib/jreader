package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.FeedDao;
import jreader.domain.Feed;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FeedDaoImplTest extends AbstractDaoTest {
    
    private static final String[] URLS = { "url_1", "url_2" };
    private static final String[] TITLES = { "title_1", "title_2" };
    private static final String[] DESCRIPTIONS = { "desc_1", "desc_2" };
    private static final String[] FEED_TYPES = { "feed_type_1", "feed_type_2" };
    private static final Long[] PUBLISHED_DATES = { 100L, 200L };
    private static List<Feed> SAVED_FEEDS;
    
    private static final String NEW_URL = "new_url";
    private static final String NEW_TITLE = "new_title";
    private static final String NEW_DESCRIPTION = "new_desc";
    private static final String NEW_FEED_TYPE = "new_feed_type";
    private static final Long NEW_PUBLISHED_DATE = 1000L;
    
    private FeedDao sut;
    
    @BeforeMethod
    public void init() {
        sut = new FeedDaoImpl();
        
        SAVED_FEEDS = new ArrayList<Feed>();
        for (int i = 0; i < URLS.length; ++i) {
            Feed feed = new Feed();
            feed.setUrl(URLS[i]);
            feed.setTitle(TITLES[i]);
            feed.setDescription(DESCRIPTIONS[i]);
            feed.setFeedType(FEED_TYPES[i]);
            feed.setPublishedDate(PUBLISHED_DATES[i]);
            SAVED_FEEDS.add(sut.save(feed));
        }
    }

    @Test
    public void find_IfFeedNotExists_ShouldReturnNull() {
        Feed feed = sut.find("url");
        
        assertNull(feed);
    }
    
    @Test
    public void save_IfFeedIsNew_ShouldReturnFeed() {
        Feed feed = new Feed();
        feed.setUrl(NEW_URL);
        feed.setTitle(NEW_TITLE);
        feed.setDescription(NEW_DESCRIPTION);
        feed.setFeedType(NEW_FEED_TYPE);
        feed.setPublishedDate(NEW_PUBLISHED_DATE);

        feed = sut.save(feed);
        
        assertNotNull(feed);
        assertEquals(feed.getUrl(), NEW_URL);
        assertEquals(feed.getTitle(), NEW_TITLE);
        assertEquals(feed.getDescription(), NEW_DESCRIPTION);
        assertEquals(feed.getFeedType(), NEW_FEED_TYPE);
        assertEquals(feed.getPublishedDate(), NEW_PUBLISHED_DATE);
    }
    
    @Test
    public void find_IfFeedExists_ShouldReturnFeed() {
        Feed feed = sut.find(URLS[0]);
        
        assertNotNull(feed);
        assertEquals(feed.getUrl(), URLS[0]);
        assertEquals(feed.getTitle(), TITLES[0]);
        assertEquals(feed.getDescription(), DESCRIPTIONS[0]);
        assertEquals(feed.getFeedType(), FEED_TYPES[0]);
        assertEquals(feed.getPublishedDate(), PUBLISHED_DATES[0]);
    }
    
    @Test
    public void listAll_IfFeedsExist_ShouldReturnAll() {
        List<Feed> feeds = sut.listAll();
        
        assertNotNull(feeds);
        assertEquals(feeds.size(), 2);
        assertEquals(feeds.get(0).getUrl(), URLS[0]);
        assertEquals(feeds.get(1).getUrl(), URLS[1]);
    }

}
