package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.FeedStatDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.FeedStat;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.FeedDto;
import jreader.dto.RssFetchResult;
import jreader.services.DateHelper;
import jreader.services.RssService;

public class CronServiceImplTest {
	
    private static final String USERNAME = "username";
	private static final String FEED_URL = "url";
	private static final String FEED_TITLE = "title";

	@InjectMocks
	private CronServiceImpl service;
	
	@Mock
	private SubscriptionDao subscriptionDao;
	@Mock
	private FeedDao feedDao;
	@Mock
	private FeedEntryDao feedEntryDao;
	@Mock
    private FeedStatDao feedStatDao;
	
	@Mock
	private RssService rssService;
	@Mock
    private ConversionService conversionService;
	
	@Mock
	private BuilderFactory builderFactory;
	
	@Mock
	private DateHelper dateHelper;
	
	@Mock
	private Feed feed1;
	@Mock
	private Feed feed2;
	
	@Mock
	private RssFetchResult fetchResult;
	
	@Mock
	private Subscription subscription1;
	@Mock
	private Subscription subscription2;
	
	@Mock
	private FeedEntry entry11;
	@Mock
	private FeedEntry entry12;
	@Mock
    private FeedEntry entry13;
	@Mock
	private FeedEntry entry21;
	@Mock
	private FeedEntry entry22;
	
	@Mock
	private FeedStat.Builder builder;
	
	@Mock
	private FeedStat feedStat1;
	@Mock
    private FeedStat feedStat2;
	@Mock
    private List<FeedStat> feedStats;
	
	@Mock
	private SubscriptionGroup group;
	
	@Mock
	private User user;
	
    @Mock
    private FeedDto dto1;
    @Mock
    private FeedDto dto2;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
    public void listFeeds() {
        when(feedDao.listAll()).thenReturn(Arrays.asList(feed1, feed2));
        when(conversionService.convert(feed1, FeedDto.class)).thenReturn(dto1);
        when(conversionService.convert(feed2, FeedDto.class)).thenReturn(dto2);
        
        List<FeedDto> dtos = service.listFeeds();
        
        verify(feedDao).listAll();
        
        verify(conversionService).convert(feed1, FeedDto.class);
        verify(conversionService).convert(feed2, FeedDto.class);
        
        assertEquals(dtos.size(), 2);
        assertEquals(dtos.get(0), dto1);
        assertEquals(dtos.get(1), dto2);
    }
	
	@Test
	public void refreshFeeds_NewEntries() {
	    when(feedDao.find(FEED_URL)).thenReturn(feed1);
		when(feed1.getUrl()).thenReturn(FEED_URL);
		when(feed1.getTitle()).thenReturn(FEED_TITLE);
		
		long date = 1445271922000L;
		when(feed1.getRefreshDate()).thenReturn(date - 1000 * 60 * 20);
		when(dateHelper.getCurrentDate()).thenReturn(date);
		
		when(rssService.fetch(FEED_URL)).thenReturn(fetchResult);
		
		when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1, subscription2));
		
		when(fetchResult.getFeedEntries()).thenReturn(Arrays.asList(entry11, entry12, entry13));
		
		when(subscription1.getUpdatedDate()).thenReturn(date - 1000 * 60 * 20);
		when(subscription2.getUpdatedDate()).thenReturn(date - 1000 * 60 * 20);
		
		DateHelper dh = new DateHelperImpl();
		long pubDate1 = date - 1000 * 60 * 30;
        when(entry11.getPublishedDate()).thenReturn(pubDate1);
		when(entry11.getUri()).thenReturn("uri1");
		long day = dh.getFirstSecondOfDay(pubDate1);
        when(dateHelper.getFirstSecondOfDay(pubDate1)).thenReturn(day);
		
		long pubDate2 = date - 1000 * 60 * 10;
        when(entry12.getPublishedDate()).thenReturn(pubDate2);
		when(entry12.getUri()).thenReturn("uri2");
		when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(dh.getFirstSecondOfDay(pubDate2));
		
		when(entry13.getPublishedDate()).thenReturn(pubDate2);
		when(entry13.getUri()).thenReturn("uri3");
		
		when(subscription1.getGroup()).thenReturn(group);
		when(subscription2.getGroup()).thenReturn(group);
		when(group.getUser()).thenReturn(user);
		when(user.getUsername()).thenReturn(USERNAME);
		
		when(builderFactory.createFeedStatBuilder()).thenReturn(builder);
		when(builder.feed(feed1)).thenReturn(builder);
		when(builder.refreshDate(anyLong())).thenReturn(builder);
		when(builder.count(anyInt())).thenReturn(builder);
		when(builder.build()).thenReturn(feedStat1);
		
		when(feedStat1.getCount()).thenReturn(1);
		
		service.refresh(FEED_URL);
		
		verify(rssService).fetch(FEED_URL);
		
		verify(subscriptionDao).listSubscriptions(feed1);
		
		verify(feedEntryDao, never()).save(entry11);
		
		verify(entry12).setSubscription(subscription1);
		verify(entry12).setSubscription(subscription2);
		verify(feedEntryDao, times(2)).save(entry12);
		
		verify(entry13).setSubscription(subscription1);
        verify(entry13).setSubscription(subscription2);
        verify(feedEntryDao, times(2)).save(entry13);
		
		verify(subscription1).setUpdatedDate(pubDate2);
		verify(subscription1).setRefreshDate(date);
		verify(subscriptionDao).save(subscription1);
		verify(subscription2).setUpdatedDate(pubDate2);
		verify(subscription2).setRefreshDate(date);
        verify(subscriptionDao).save(subscription2);
        
        verify(feed1).setRefreshDate(date);
        verify(feedDao).save(feed1);
        
        verify(builder).feed(feed1);
        verify(builder).refreshDate(day);
        verify(builder).count(1);
        verify(builder).build();
        verify(feedStat1).setCount(2);
        verify(feedStatDao).saveAll(Arrays.asList(feedStat1));
	}
	
	@Test
	public void isNew_Feed_PublishedDateIsNull() {
	    when(entry11.getPublishedDate()).thenReturn(null);
	    
	    boolean isNew = service.isNew(entry11, feed1, 0L);
	    
	    assertFalse(isNew);
	}
	
	@Test
    public void isNew_Feed_UriIsNull() {
        when(entry11.getPublishedDate()).thenReturn(1000L);
        when(entry11.getUri()).thenReturn(null);
        
        boolean isNew = service.isNew(entry11, feed1, 0L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Feed_FeedRefreshDateIsNull() {
	    when(entry11.getPublishedDate()).thenReturn(1000L);
        when(entry11.getUri()).thenReturn("uri");
        
        boolean isNew = service.isNew(entry11, feed1, null);
        
        assertTrue(isNew);
    }
	
	@Test
    public void isNew_Feed_PublishedDateIsSmaller() {
        when(entry11.getPublishedDate()).thenReturn(999L);
        when(entry11.getUri()).thenReturn("uri");
        
        boolean isNew = service.isNew(entry11, feed1, 1000L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Feed_PublishedDateIsEqual() {
        when(entry11.getPublishedDate()).thenReturn(1000L);
        when(entry11.getUri()).thenReturn("uri");
        
        boolean isNew = service.isNew(entry11, feed1, 1000L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Feed_PublishedDateIsLarger() {
        when(entry11.getPublishedDate()).thenReturn(1001L);
        when(entry11.getUri()).thenReturn("uri");
        
        boolean isNew = service.isNew(entry11, feed1, 1000L);
        
        assertTrue(isNew);
    }
	
	@Test
    public void isNew_Subscription_PublishedDateIsNull() {
        when(entry11.getPublishedDate()).thenReturn(null);
        
        boolean isNew = service.isNew(entry11, subscription1, 0L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_UriIsNull() {
        when(entry11.getPublishedDate()).thenReturn(1000L);
        when(entry11.getUri()).thenReturn(null);
        
        boolean isNew = service.isNew(entry11, subscription1, 0L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_PublishedDateIsSmaller() {
        when(entry11.getPublishedDate()).thenReturn(1000L);
        when(entry11.getUri()).thenReturn("uri");
        
        boolean isNew = service.isNew(entry11, subscription1, 1100L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_PublishedDateIsEqual() {
        when(entry11.getPublishedDate()).thenReturn(1000L);
        when(entry11.getUri()).thenReturn("uri");
        when(feedEntryDao.find(subscription1, "uri")).thenReturn(null);
        
        boolean isNew = service.isNew(entry11, subscription1, 1000L);
        
        assertTrue(isNew);
    }
	
	@Test
    public void isNew_Subscription_EntryFound() {
        when(entry11.getPublishedDate()).thenReturn(1000L);
        when(entry11.getUri()).thenReturn("uri");
        when(feedEntryDao.find(subscription1, "uri")).thenReturn(entry11);
        
        boolean isNew = service.isNew(entry11, subscription1, 900L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_EntryNotFound() {
        when(entry11.getPublishedDate()).thenReturn(1000L);
        when(entry11.getUri()).thenReturn("uri");
        when(feedEntryDao.find(subscription1, "uri")).thenReturn(null);
        
        boolean isNew = service.isNew(entry11, subscription1, 900L);
        
        assertTrue(isNew);
    }
	
	@Test
	public void updateFeed_SameDay() {
	    long lastRefreshDate = 850L;
        when(feed1.getRefreshDate()).thenReturn(lastRefreshDate);
	    when(fetchResult.getFeedEntries()).thenReturn(Arrays.asList(entry11, entry12, entry13));
	    
	    long currentDate = 1000L;
	    
	    long pubDate1 = 700L;
        when(entry11.getPublishedDate()).thenReturn(pubDate1);
        when(entry11.getUri()).thenReturn("uri1");
        
	    long pubDate2 = 900L;
        when(entry12.getPublishedDate()).thenReturn(pubDate2);
        when(entry12.getUri()).thenReturn("uri2");
        long day = 800L;
        when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(day);
        
        long pubDate3 = 900L;
        when(entry13.getPublishedDate()).thenReturn(pubDate3);
        when(entry13.getUri()).thenReturn("uri3");
        when(dateHelper.getFirstSecondOfDay(pubDate3)).thenReturn(day);
	    
        when(builderFactory.createFeedStatBuilder()).thenReturn(builder);
        when(builder.feed(feed1)).thenReturn(builder);
        when(builder.refreshDate(anyLong())).thenReturn(builder);
        when(builder.count(anyInt())).thenReturn(builder);
        when(builder.build()).thenReturn(feedStat1);
        when(feedStat1.getCount()).thenReturn(1);
        
	    service.updateFeed(feed1, currentDate, fetchResult);
	    
	    verify(feed1).setRefreshDate(currentDate);
	    verify(feedDao).save(feed1);
	    
	    verify(builder).feed(feed1);
        verify(builder).refreshDate(day);
        verify(builder).count(1);
        verify(builder).build();
        verify(feedStat1).setCount(2);
        verify(feedStatDao).saveAll(Arrays.asList(feedStat1));
	}
	
	@Test
    public void updateFeed_DifferentDay() {
        long lastRefreshDate = 850L;
        when(feed1.getRefreshDate()).thenReturn(lastRefreshDate);
        when(fetchResult.getFeedEntries()).thenReturn(Arrays.asList(entry11, entry12, entry13));
        
        long currentDate = 1000L;
        
        long pubDate1 = 700L;
        when(entry11.getPublishedDate()).thenReturn(pubDate1);
        when(entry11.getUri()).thenReturn("uri1");
        
        long pubDate2 = 900L;
        when(entry12.getPublishedDate()).thenReturn(pubDate2);
        when(entry12.getUri()).thenReturn("uri2");
        long day2 = 800L;
        when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(day2);
        
        long pubDate3 = 950L;
        when(entry13.getPublishedDate()).thenReturn(pubDate3);
        when(entry13.getUri()).thenReturn("uri3");
        long day3 = 900L;
        when(dateHelper.getFirstSecondOfDay(pubDate3)).thenReturn(day3);
        
        when(feedStatDao.find(feed1, day2)).thenReturn(feedStat1);
        
        when(feedStat1.getCount()).thenReturn(5);
        
        when(builderFactory.createFeedStatBuilder()).thenReturn(builder);
        when(builder.feed(feed1)).thenReturn(builder);
        when(builder.refreshDate(anyLong())).thenReturn(builder);
        when(builder.count(anyInt())).thenReturn(builder);
        when(builder.build()).thenReturn(feedStat2);
        when(feedStat2.getCount()).thenReturn(1);
        
        service.updateFeed(feed1, currentDate, fetchResult);
        
        verify(feed1).setRefreshDate(currentDate);
        verify(feedDao).save(feed1);
        
        verify(feedStat1).setCount(6);
        
        verify(builder).feed(feed1);
        verify(builder).refreshDate(day3);
        verify(builder).count(1);
        verify(builder).build();
        
        verify(feedStatDao).saveAll(Arrays.asList(feedStat1, feedStat2));
    }
	
	@Test
	public void cleanUp() {
		when(feedDao.find(FEED_URL)).thenReturn(feed1);
		
		when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1, subscription2));
		
		when(feedEntryDao.find(subscription1, 1)).thenReturn(entry12);
		when(feedEntryDao.find(subscription2, 1)).thenReturn(entry22);
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(2015, 6, 6, 0, 25, 0);
		long threshold = cal.getTimeInMillis();
		when(dateHelper.substractDaysFromCurrentDate(30)).thenReturn(threshold);
		
        cal.set(2015, 6, 6, 0, 0, 0);
		when(entry11.getPublishedDate()).thenReturn(cal.getTimeInMillis());
		cal.set(2015, 6, 6, 0, 10, 0);
		long publishedDate12 = cal.getTimeInMillis();
		when(entry12.getPublishedDate()).thenReturn(publishedDate12);
		cal.set(2015, 6, 6, 0, 20, 0);
		when(entry21.getPublishedDate()).thenReturn(cal.getTimeInMillis());
		cal.set(2015, 6, 6, 0, 30, 0);
		when(entry22.getPublishedDate()).thenReturn(cal.getTimeInMillis());
		
		when(feedEntryDao.listUnstarredOlderThan(eq(subscription1), anyLong())).thenReturn(Arrays.asList(entry12));
		when(feedEntryDao.listUnstarredOlderThan(eq(subscription2), anyLong())).thenReturn(Arrays.asList(entry22));
		
		when(subscription1.getGroup()).thenReturn(group);
		when(subscription2.getGroup()).thenReturn(group);
		when(group.getUser()).thenReturn(user);
		
		service.cleanup(FEED_URL, 30, 1);
		
		verify(feedDao).find(FEED_URL);
		
		verify(subscriptionDao).listSubscriptions(feed1);
		
		verify(feedEntryDao).find(subscription1, 1);
		verify(feedEntryDao).find(subscription2, 1);
		
		verify(feedEntryDao).listUnstarredOlderThan(eq(subscription1), eq(publishedDate12));
		verify(feedEntryDao).delete(entry12);
		
		verify(feedEntryDao).listUnstarredOlderThan(eq(subscription2), eq(threshold));
		verify(feedEntryDao).delete(entry22);
		verifyNoMoreInteractions(feedEntryDao);
	}
	
	@Test
    public void cleanUpFeedWithoutSubscription() {
        when(feedDao.find(FEED_URL)).thenReturn(feed1);
        when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Collections.<Subscription>emptyList());
        when(feedStatDao.list(feed1)).thenReturn(feedStats);
        
        service.cleanup(FEED_URL, 1, 1);
        
        verify(feedDao).find(FEED_URL);
        verify(subscriptionDao).listSubscriptions(feed1);
        verify(feedStatDao).deleteAll(feedStats);
        verify(feedDao).delete(feed1);
    }

}
