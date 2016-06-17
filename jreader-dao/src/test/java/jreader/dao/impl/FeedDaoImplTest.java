package jreader.dao.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedDao;
import jreader.domain.Feed;

public class FeedDaoImplTest extends AbstractDaoTest {

    private FeedDao sut;

    private static List<Feed> feeds;

    @BeforeMethod
    public void init() {
        sut = new FeedDaoImpl();
        
        List<Feed> feedsToBeSaved = Arrays.asList(
                Feed.builder().url("url_1").title("title_1").description("desc_1").feedType("feed_type_1").lastUpdateDate(100L).lastRefreshDate(200L)
                        .status(0).build(),
                        Feed.builder().url("url_2").title("title_2").description("desc_2").feedType("feed_type_2").lastUpdateDate(300L).lastRefreshDate(400L)
                        .status(1).build());
        feeds = new ArrayList<Feed>();
        for (Feed feed : feedsToBeSaved) {
            feeds.add(sut.save(feed));
        }
    }

    @Test
    public void find_IfFeedNotExists_ShouldReturnNull() {
        Feed actual = sut.find("url");
        
        assertNull(actual);
    }
    
    @Test
    public void save_IfFeedIsNew_ShouldReturnFeed() {
        Feed feed = Feed.builder().url("new_url").title("new_title").description("new_desc").feedType("new_feed_type").lastUpdateDate(1000L)
                .lastRefreshDate(2000L).status(2).build();

        Feed actual = sut.save(feed);
        
        assertEquals(actual, feed);
    }
    
    @Test
    public void find_IfFeedExists_ShouldReturnFeed() {
        Feed actual = sut.find(feeds.get(0).getUrl());
        
        assertEquals(actual, feeds.get(0));
    }
    
    @Test
    public void listAll_IfFeedsExist_ShouldReturnAll() {
        List<Feed> actual = sut.listAll();
        
        assertEquals(actual, feeds);
    }

}
