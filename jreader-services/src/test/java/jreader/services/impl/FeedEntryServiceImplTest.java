package jreader.services.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.FeedEntryDto;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryFilterData.Group;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FeedEntryServiceImplTest {
	
	private static final String USERNAME = "user";
	private static final Long GROUP_ID_1 = 1L;
	private static final Long GROUP_ID_2 = 2L;
	private static final Long SUBSCRIPTION_ID_1 = 11L;
	private static final Long SUBSCRIPTION_ID_2 = 12L;
	private static final Long ENTRY_ID_1 = 21L;
	private static final Long ENTRY_ID_2 = 22L;
	
	@InjectMocks
	private FeedEntryServiceImpl service;

	@Mock
	private UserDao userDao;
	@Mock
	private SubscriptionGroupDao subscriptionGroupDao;
	@Mock
	private SubscriptionDao subscriptionDao;
	@Mock
	private FeedEntryDao feedEntryDao;
	@Mock
	private ConversionService conversionService;
	
	@Mock
	private User user;
	@Mock
	private SubscriptionGroup group1;
	@Mock
	private SubscriptionGroup group2;
	@Mock
	private Subscription subscription1;
	@Mock
	private Subscription subscription2;
	@Mock
	private FeedEntry entry1;
	@Mock
	private FeedEntry entry2;
	@Mock
	private FeedEntryFilterData filter;
	@Mock
	private FeedEntryDto entryDto1;
	@Mock
	private FeedEntryDto entryDto2;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void markRead() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(subscriptionGroupDao.find(user, GROUP_ID_2)).thenReturn(group2);
		when(subscriptionDao.find(group1, SUBSCRIPTION_ID_1)).thenReturn(subscription1);
		when(subscriptionDao.find(group2, SUBSCRIPTION_ID_2)).thenReturn(subscription2);
		when(feedEntryDao.find(subscription1, ENTRY_ID_1)).thenReturn(entry1);
		when(feedEntryDao.find(subscription2, ENTRY_ID_2)).thenReturn(entry2);
		when(entry1.isRead()).thenReturn(false);
		when(entry2.isRead()).thenReturn(false);
		
		service.markRead(USERNAME, Arrays.asList(GROUP_ID_1, GROUP_ID_2), Arrays.asList(SUBSCRIPTION_ID_1, SUBSCRIPTION_ID_2), Arrays.asList(ENTRY_ID_1, ENTRY_ID_2));
		
		verify(entry1).setRead(true);
		verify(entry2).setRead(true);
		verify(feedEntryDao).saveAll(Arrays.asList(entry1, entry2));
	}
	
	@Test
	public void star() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(subscriptionDao.find(group1, SUBSCRIPTION_ID_1)).thenReturn(subscription1);
		when(feedEntryDao.find(subscription1, ENTRY_ID_1)).thenReturn(entry1);
		when(entry1.isStarred()).thenReturn(false);
		
		service.star(USERNAME, GROUP_ID_1, SUBSCRIPTION_ID_1, ENTRY_ID_1);
		
		verify(entry1).setStarred(true);
		verify(feedEntryDao).save(entry1);
	}
	
	@Test
	public void unstar() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(subscriptionDao.find(group1, SUBSCRIPTION_ID_1)).thenReturn(subscription1);
		when(feedEntryDao.find(subscription1, ENTRY_ID_1)).thenReturn(entry1);
		when(entry1.isStarred()).thenReturn(true);
		
		service.unstar(USERNAME, GROUP_ID_1, SUBSCRIPTION_ID_1, ENTRY_ID_1);
		
		verify(entry1).setStarred(false);
		verify(feedEntryDao).save(entry1);
	}
	
	@Test
	public void listForUser() {
		when(filter.getGroup()).thenReturn(Group.ALL);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(feedEntryDao.list(user, filter)).thenReturn(Arrays.asList(entry1, entry2));
		when(conversionService.convert(entry1, FeedEntryDto.class)).thenReturn(entryDto1);
		when(conversionService.convert(entry2, FeedEntryDto.class)).thenReturn(entryDto2);
		
		List<FeedEntryDto> result = service.listEntries(filter);
		
		verify(feedEntryDao).list(user, filter);
		Assert.assertEquals(result, Arrays.asList(entryDto1, entryDto2));
	}
	
	@Test
	public void listForGroup() {
		when(filter.getGroup()).thenReturn(Group.SUBSCRIPTION_GROUP);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(filter.getSubscriptionGroupId()).thenReturn(GROUP_ID_1);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(feedEntryDao.list(group1, filter)).thenReturn(Arrays.asList(entry1, entry2));
		when(conversionService.convert(entry1, FeedEntryDto.class)).thenReturn(entryDto1);
		when(conversionService.convert(entry2, FeedEntryDto.class)).thenReturn(entryDto2);
		
		List<FeedEntryDto> result = service.listEntries(filter);
		
		verify(feedEntryDao).list(group1, filter);
		Assert.assertEquals(result, Arrays.asList(entryDto1, entryDto2));
	}
	
	@Test
	public void listForSubscription() {
		when(filter.getGroup()).thenReturn(Group.SUBSCRIPTION);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(filter.getSubscriptionGroupId()).thenReturn(GROUP_ID_1);
		when(filter.getSubscriptionId()).thenReturn(SUBSCRIPTION_ID_1);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(subscriptionDao.find(group1, SUBSCRIPTION_ID_1)).thenReturn(subscription1);
		when(feedEntryDao.list(subscription1, filter)).thenReturn(Arrays.asList(entry1, entry2));
		when(conversionService.convert(entry1, FeedEntryDto.class)).thenReturn(entryDto1);
		when(conversionService.convert(entry2, FeedEntryDto.class)).thenReturn(entryDto2);
		
		List<FeedEntryDto> result = service.listEntries(filter);
		
		verify(feedEntryDao).list(subscription1, filter);
		Assert.assertEquals(result, Arrays.asList(entryDto1, entryDto2));
	}
	
}
