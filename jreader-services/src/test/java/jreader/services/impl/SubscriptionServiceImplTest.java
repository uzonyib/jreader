package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.RssFetchResult;
import jreader.dto.SubscriptionDto;
import jreader.services.RssService;
import jreader.services.ServiceException;

public class SubscriptionServiceImplTest {
	
	private static final String USERNAME = "user";
	private static final String FEED_TITLE = "feed_title";
	private static final String URL = "url";
	private static final int SUBSCRIPTION_ORDER = 10;
	private static final long GROUP_ID = 123;
	private static final long SUBSCRIPTION_ID = 456;
	private static final String SUBSCRIPTION_TITLE = "subscription_title";
	
	@InjectMocks
	private SubscriptionServiceImpl service;

	@Mock
	private UserDao userDao;
	@Mock
	private SubscriptionGroupDao subscriptionGroupDao;
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
	private BuilderFactory builderFactory;
	@Mock
    private Subscription.Builder subscriptionBuilder;
	
	@Mock
	private User user;
	@Mock
	private SubscriptionGroup group;
	@Mock
	private Subscription subscription;
	@Mock
	private Subscription subscription1;
	@Mock
	private Subscription subscription2;
	@Mock
	private Feed feed;
	@Mock
	private RssFetchResult fetchResult;
	@Mock
	private FeedEntry entry1;
	@Mock
	private FeedEntry entry2;
	
	@Mock
	private SubscriptionDto subscriptionDto;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void subscribeToNewFeed() {
		when(fetchResult.getFeed()).thenReturn(feed);
		when(feed.getTitle()).thenReturn(FEED_TITLE);
		when(fetchResult.getFeedEntries()).thenReturn(Arrays.asList(entry1, entry2));
		when(entry1.getPublishedDate()).thenReturn(1000L);
		when(entry1.getPublishedDate()).thenReturn(2000L);
		
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		when(rssService.fetch(URL)).thenReturn(fetchResult);
		when(feedDao.find(URL)).thenReturn(null);
		when(feedDao.save(feed)).thenReturn(feed);
		when(subscriptionDao.find(user, feed)).thenReturn(null);
		when(subscriptionDao.getMaxOrder(group)).thenReturn(SUBSCRIPTION_ORDER - 1);
		when(builderFactory.createSubscriptionBuilder()).thenReturn(subscriptionBuilder);
		when(subscriptionBuilder.group(group)).thenReturn(subscriptionBuilder);
		when(subscriptionBuilder.feed(feed)).thenReturn(subscriptionBuilder);
		when(subscriptionBuilder.title(FEED_TITLE)).thenReturn(subscriptionBuilder);
		when(subscriptionBuilder.order(SUBSCRIPTION_ORDER)).thenReturn(subscriptionBuilder);
		when(subscriptionBuilder.updatedDate(2000L)).thenReturn(subscriptionBuilder);
		when(subscriptionBuilder.refreshDate(anyLong())).thenReturn(subscriptionBuilder);
		when(subscriptionBuilder.build()).thenReturn(subscription);
		when(subscriptionDao.save(subscription)).thenReturn(subscription);
		when(conversionService.convert(subscription, SubscriptionDto.class)).thenReturn(subscriptionDto);
		
		SubscriptionDto result = service.subscribe(USERNAME, GROUP_ID, URL);
		assertEquals(result, subscriptionDto);
		
		verify(userDao).find(USERNAME);
		verify(subscriptionGroupDao).find(user, GROUP_ID);
		verify(rssService).fetch(URL);
		verify(feedDao).find(URL);
		verify(feedDao).save(feed);
		verify(subscriptionDao).find(user, feed);
		verify(subscriptionDao).save(subscription);
		verifyZeroInteractions(feedEntryDao);
	}
	
	@Test
	public void subscribeToExistingFeed() {
		when(fetchResult.getFeed()).thenReturn(feed);
		when(feed.getTitle()).thenReturn(FEED_TITLE);
		when(fetchResult.getFeedEntries()).thenReturn(Arrays.asList(entry1, entry2));
		when(entry1.getPublishedDate()).thenReturn(1000L);
		when(entry1.getPublishedDate()).thenReturn(2000L);
		
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		when(rssService.fetch(URL)).thenReturn(fetchResult);
		when(feedDao.find(URL)).thenReturn(feed);
		when(subscriptionDao.find(user, feed)).thenReturn(null);
		when(subscriptionDao.getMaxOrder(group)).thenReturn(SUBSCRIPTION_ORDER - 1);
		when(builderFactory.createSubscriptionBuilder()).thenReturn(subscriptionBuilder);
        when(subscriptionBuilder.group(group)).thenReturn(subscriptionBuilder);
        when(subscriptionBuilder.feed(feed)).thenReturn(subscriptionBuilder);
        when(subscriptionBuilder.title(FEED_TITLE)).thenReturn(subscriptionBuilder);
        when(subscriptionBuilder.order(SUBSCRIPTION_ORDER)).thenReturn(subscriptionBuilder);
        when(subscriptionBuilder.updatedDate(2000L)).thenReturn(subscriptionBuilder);
        when(subscriptionBuilder.refreshDate(anyLong())).thenReturn(subscriptionBuilder);
        when(subscriptionBuilder.build()).thenReturn(subscription);
        when(subscriptionDao.save(subscription)).thenReturn(subscription);
		
		service.subscribe(USERNAME, GROUP_ID, URL);
		
		verify(userDao).find(USERNAME);
		verify(subscriptionGroupDao).find(user, GROUP_ID);
		verifyZeroInteractions(rssService);
		verify(feedDao).find(URL);
		verify(feedDao, never()).save(feed);
		verify(subscriptionDao).find(user, feed);
		verify(subscriptionDao).save(subscription);
		verifyZeroInteractions(feedEntryDao);
	}
	
	@Test
	public void subscribeExisting() {
		when(fetchResult.getFeed()).thenReturn(feed);
		when(feed.getTitle()).thenReturn(FEED_TITLE);
		when(fetchResult.getFeedEntries()).thenReturn(Arrays.asList(entry1, entry2));
		when(entry1.getPublishedDate()).thenReturn(1000L);
		when(entry1.getPublishedDate()).thenReturn(2000L);
		
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		when(rssService.fetch(URL)).thenReturn(fetchResult);
		when(feedDao.find(URL)).thenReturn(feed);
		when(subscriptionDao.find(user, feed)).thenReturn(subscription);
		
		try {
			service.subscribe(USERNAME, GROUP_ID, URL);
			fail();
		} catch(ServiceException e) {
			assertEquals(e.getStatus(), HttpStatus.CONFLICT);
		}
		
		verify(userDao).find(USERNAME);
		verifyZeroInteractions(subscriptionGroupDao);
		verifyZeroInteractions(rssService);
		verify(feedDao).find(URL);
		verify(feedDao, never()).save(feed);
		verify(subscriptionDao).find(user, feed);
		verify(subscriptionDao, never()).save(subscription);
		verifyZeroInteractions(feedEntryDao);
	}
	
	@Test
	public void unsubscribe() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		when(subscriptionDao.find(group, SUBSCRIPTION_ID)).thenReturn(subscription);
		List<FeedEntry> entries = Arrays.asList(entry1, entry2);
		when(feedEntryDao.list(subscription)).thenReturn(entries);
		
		service.unsubscribe(USERNAME, GROUP_ID, SUBSCRIPTION_ID);
		
		verify(feedEntryDao).deleteAll(entries);
		verify(subscriptionDao).delete(subscription);
	}
	
	@Test
	public void entitleSubscription() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		when(subscriptionDao.find(group, SUBSCRIPTION_ID)).thenReturn(subscription);
		
		service.entitle(USERNAME, GROUP_ID, SUBSCRIPTION_ID, SUBSCRIPTION_TITLE);
		
		verify(subscription).setTitle(SUBSCRIPTION_TITLE);
		verify(subscriptionDao).save(subscription);
	}
	
	@Test
	public void moveSubscriptionUp() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription, subscription1, subscription2));
		final long id = 100L;
		when(subscription.getId()).thenReturn(SUBSCRIPTION_ID);
		when(subscription1.getId()).thenReturn(id);
		when(subscription2.getId()).thenReturn(200L);
		
		when(subscription.getOrder()).thenReturn(1);
		when(subscription1.getOrder()).thenReturn(2);
		when(subscription2.getOrder()).thenReturn(3);
		
		service.moveUp(USERNAME, GROUP_ID, id);
		
		verify(subscription).setOrder(2);
		verify(subscription1).setOrder(1);
		verify(subscriptionDao).saveAll(Arrays.asList(subscription, subscription1));
		verify(subscription2, never()).setOrder(anyInt());
	}
	
	@Test
	public void moveSubscriptionDown() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription, subscription1, subscription2));
		final long id = 100L;
		when(subscription.getId()).thenReturn(SUBSCRIPTION_ID);
		when(subscription1.getId()).thenReturn(id);
		when(subscription2.getId()).thenReturn(200L);
		
		when(subscription.getOrder()).thenReturn(1);
		when(subscription1.getOrder()).thenReturn(2);
		when(subscription2.getOrder()).thenReturn(3);
		
		service.moveDown(USERNAME, GROUP_ID, id);
		
		verify(subscription1).setOrder(3);
		verify(subscription2).setOrder(2);
		verify(subscriptionDao).saveAll(Arrays.asList(subscription1, subscription2));
		verify(subscription, never()).setOrder(anyInt());
	}

}
