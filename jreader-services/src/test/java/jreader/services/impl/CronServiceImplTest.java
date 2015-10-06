package jreader.services.impl;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
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
	private RssService rssService;
	@Mock
    private ConversionService conversionService;
	
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
	public void refreshFeeds() {
	    when(feedDao.find(FEED_URL)).thenReturn(feed1);
		when(feed1.getUrl()).thenReturn(FEED_URL);
		when(feed1.getTitle()).thenReturn(FEED_TITLE);
		
		when(rssService.fetch(FEED_URL)).thenReturn(fetchResult);
		
		when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1, subscription2));
		
		when(fetchResult.getFeedEntries()).thenReturn(Arrays.asList(entry11, entry12, entry13));
		
		when(subscription1.getUpdatedDate()).thenReturn(10L);
		when(subscription2.getUpdatedDate()).thenReturn(10L);
		
		when(entry11.getPublishedDate()).thenReturn(9L);
		when(entry11.getUri()).thenReturn("uri1");
		
		when(entry12.getPublishedDate()).thenReturn(11L);
		when(entry12.getUri()).thenReturn("uri2");
		
		when(entry13.getPublishedDate()).thenReturn(11L);
		when(entry13.getUri()).thenReturn("uri3");
		
		when(subscription1.getGroup()).thenReturn(group);
		when(subscription2.getGroup()).thenReturn(group);
		when(group.getUser()).thenReturn(user);
		when(user.getUsername()).thenReturn(USERNAME);
		
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
		
		verify(subscription1).setUpdatedDate(11L);
		verify(subscriptionDao).save(subscription1);
		verify(subscription2).setUpdatedDate(11L);
        verify(subscriptionDao).save(subscription2);
	}
	
	@Test
	public void cleanUp() {
		when(feedDao.find(FEED_URL)).thenReturn(feed1);
		
		when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1, subscription2));
		
		when(feedEntryDao.find(subscription1, 1)).thenReturn(entry12);
		when(feedEntryDao.find(subscription2, 1)).thenReturn(entry22);
		
		Calendar cal = Calendar.getInstance();
		cal.set(2015, 6, 6, 0, 25, 0);
		long threshold = cal.getTimeInMillis();
		when(dateHelper.addDaysToCurrentDate(-30)).thenReturn(threshold);
		
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
        
        service.cleanup(FEED_URL, 1, 1);
        
        verify(feedDao).find(FEED_URL);
        verify(subscriptionDao).listSubscriptions(feed1);
        verify(feedDao).delete(feed1);
    }

}
