package jreader.services.impl;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.RssFetchResult;
import jreader.services.RssService;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CronServiceImplTest {
	
	private static final String URL_1 = "url1";
	private static final String URL_2 = "url2";

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
	private Feed feed1;
	@Mock
	private Feed feed2;
	
	@Mock
	private RssFetchResult fetchResult1;
	@Mock
	private RssFetchResult fetchResult2;
	
	@Mock
	private Subscription subscription1;
	@Mock
	private Subscription subscription2;
	
	@Mock
	private FeedEntry entry11;
	@Mock
	private FeedEntry entry12;
	@Mock
	private FeedEntry entry21;
	@Mock
	private FeedEntry entry22;
	
	@Mock
	private SubscriptionGroup group;
	
	@Mock
	private User user;
	
	@Captor
	private ArgumentCaptor<Long> dateCaptor;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void refreshFeeds() {
		when(feed1.getUrl()).thenReturn(URL_1);
		when(feed2.getUrl()).thenReturn(URL_2);
		
		when(feedDao.listAll()).thenReturn(Arrays.asList(feed1, feed2));
		
		when(rssService.fetch(URL_1)).thenReturn(fetchResult1);
		when(rssService.fetch(URL_2)).thenReturn(fetchResult2);
		
		when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1));
		when(subscriptionDao.listSubscriptions(feed2)).thenReturn(Arrays.asList(subscription2));
		
		when(fetchResult1.getFeedEntries()).thenReturn(Arrays.asList(entry11, entry12));
		when(fetchResult2.getFeedEntries()).thenReturn(Arrays.asList(entry21, entry22));
		
		when(subscription1.getUpdatedDate()).thenReturn(10L);
		when(subscription2.getUpdatedDate()).thenReturn(10L);
		
		when(entry11.getPublishedDate()).thenReturn(9L);
		when(entry12.getPublishedDate()).thenReturn(11L);
		when(entry21.getPublishedDate()).thenReturn(10L);
		when(entry22.getPublishedDate()).thenReturn(20L);
		
		when(subscription1.getGroup()).thenReturn(group);
		when(subscription2.getGroup()).thenReturn(group);
		when(group.getUser()).thenReturn(user);
		
		service.refreshFeeds();
		
		verify(feedDao).listAll();
		
		verify(rssService).fetch(URL_1);
		verify(rssService).fetch(URL_2);
		
		verify(subscriptionDao).listSubscriptions(feed1);
		verify(subscriptionDao).listSubscriptions(feed2);
		
		verify(feedEntryDao, never()).save(entry11);
		verify(entry12).setSubscription(subscription1);
		verify(feedEntryDao).save(entry12);
		
		verify(feedEntryDao, never()).save(entry21);
		verify(entry22).setSubscription(subscription2);
		verify(feedEntryDao).save(entry22);
		
		verify(subscription1).setUpdatedDate(11L);
		verify(subscriptionDao).save(subscription1);
		
		verify(subscription2).setUpdatedDate(20L);
		verify(subscriptionDao).save(subscription2);
	}
	
	@Test
	public void cleanUp() {
		when(feedDao.listAll()).thenReturn(Arrays.asList(feed1, feed2));
		
		when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1));
		when(subscriptionDao.listSubscriptions(feed2)).thenReturn(Arrays.asList(subscription2));
		
		when(feedEntryDao.find(subscription1, 1)).thenReturn(entry11);
		when(feedEntryDao.find(subscription2, 1)).thenReturn(entry21);
		
		long start = System.currentTimeMillis();
		when(entry11.getPublishedDate()).thenReturn(start - 1000 * 60 * 60 * 25);
		when(entry12.getPublishedDate()).thenReturn(start - 1000 * 60 * 60 * 26);
		when(entry21.getPublishedDate()).thenReturn(start - 1000 * 60 * 60 * 27);
		when(entry22.getPublishedDate()).thenReturn(start - 1000 * 60 * 60 * 28);
		
		when(feedEntryDao.listUnstarredOlderThan(eq(subscription1), anyLong())).thenReturn(Arrays.asList(entry12));
		when(feedEntryDao.listUnstarredOlderThan(eq(subscription2), anyLong())).thenReturn(Arrays.asList(entry22));
		
		when(subscription1.getGroup()).thenReturn(group);
		when(subscription2.getGroup()).thenReturn(group);
		when(group.getUser()).thenReturn(user);
		
		service.cleanup(1, 1);
		long end = System.currentTimeMillis();
		
		verify(feedDao).listAll();
		
		verify(subscriptionDao).listSubscriptions(feed1);
		verify(subscriptionDao).listSubscriptions(feed2);
		
		verify(feedEntryDao).find(subscription1, 1);
		verify(feedEntryDao).find(subscription2, 1);
		
		verify(feedEntryDao).listUnstarredOlderThan(eq(subscription1), dateCaptor.capture());
		assertTrue(dateCaptor.getValue() >= start - 1000 * 60 * 60 * 25);
		assertTrue(dateCaptor.getValue() <= end - 1000 * 60 * 60 * 25);
		verify(feedEntryDao).delete(entry12);
		
		verify(feedEntryDao).listUnstarredOlderThan(eq(subscription2), dateCaptor.capture());
		assertTrue(dateCaptor.getValue() >= start - 1000 * 60 * 60 * 27);
		assertTrue(dateCaptor.getValue() <= end - 1000 * 60 * 60 * 27);
		verify(feedEntryDao).delete(entry22);
	}

}
