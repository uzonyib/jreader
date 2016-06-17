package jreader.dao.impl;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedDao;
import jreader.dao.FeedStatDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;

public class FeedStatDaoImplTest extends AbstractDaoTest {
    
    private FeedDao feedDao;

    private FeedStatDao sut;
    
    private static Feed feed;
    private static List<FeedStat> stats;
    
    @BeforeMethod
    public void init() {
        feedDao = new FeedDaoImpl();
        sut = new FeedStatDaoImpl();
        
        Feed feedToBeSaved = Feed.builder().url("url").title("title").description("description").feedType("feedType").lastUpdateDate(1000L)
                .lastRefreshDate(2000L).status(1).build();
        feed = feedDao.save(feedToBeSaved);
        
        List<FeedStat> statsToBeSaved = Arrays.asList(
                new FeedStat(feed, 100L, 1),
                new FeedStat(feed, 110L, 2),
                new FeedStat(feed, 120L, 3),
                new FeedStat(feed, 130L, 4));
        stats = new ArrayList<FeedStat>();
        for (FeedStat stat : statsToBeSaved) {
            stats.add(sut.save(stat));
        }
    }
    
    @Test
    public void list_ShouldReturnAll() {
        List<FeedStat> actual = sut.list(feed);
        
        assertEquals(actual, stats);
    }
    
    @Test
    public void find_ShouldReturnMatchingStat() {
        FeedStat actual = sut.find(feed, 110L);
        
        assertEquals(actual, stats.get(1));
    }

    @Test
    public void listAfter_ShouldReturnMatchingStats() {
        List<FeedStat> actual = sut.listAfter(feed, 115L);
        
        assertEquals(actual, Arrays.asList(stats.get(2), stats.get(3)));
    }
    
    @Test
    public void listBefore_ShouldReturnMatchingStats() {
        List<FeedStat> actual = sut.listBefore(feed, 115L);
        
        assertEquals(actual, Arrays.asList(stats.get(0), stats.get(1)));
    }

}
