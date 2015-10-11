package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.SubscriptionDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.ServiceException;

public class SubscriptionGroupServiceImplTest {
	
	private static final String USERNAME = "user";
	private static final String GROUP_TITLE = "group_title";
	private static final int GROUP_ORDER = 10;
	private static final long GROUP_ID = 123;
	
	@InjectMocks
	private SubscriptionGroupServiceImpl service;

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
	private BuilderFactory builderFactory;
	@Mock
	private SubscriptionGroup.Builder groupBuilder;
	
	@Mock
	private User user;
	@Mock
	private SubscriptionGroup group;
	@Mock
	private SubscriptionGroup group1;
	@Mock
	private SubscriptionGroup group2;
	@Mock
	private Subscription subscription;
	@Mock
	private Subscription subscription1;
	@Mock
	private Subscription subscription2;
	
	@Mock
	private SubscriptionGroupDto groupDto;
	@Mock
	private SubscriptionGroupDto groupDto1;
	@Mock
	private SubscriptionGroupDto groupDto2;
	@Mock
	private SubscriptionDto subscriptionDto;
	@Mock
	private SubscriptionDto subscriptionDto1;
	@Mock
	private SubscriptionDto subscriptionDto2;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void createNewGroup() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_TITLE)).thenReturn(null);
		when(subscriptionGroupDao.getMaxOrder(user)).thenReturn(GROUP_ORDER - 1);
		when(builderFactory.createGroupBuilder()).thenReturn(groupBuilder);
		when(groupBuilder.user(user)).thenReturn(groupBuilder);
		when(groupBuilder.title(GROUP_TITLE)).thenReturn(groupBuilder);
		when(groupBuilder.order(GROUP_ORDER)).thenReturn(groupBuilder);
		when(groupBuilder.build()).thenReturn(group);
		when(subscriptionGroupDao.save(group)).thenReturn(group);
		when(conversionService.convert(group, SubscriptionGroupDto.class)).thenReturn(groupDto);
		
		SubscriptionGroupDto result = service.create(USERNAME, GROUP_TITLE);
		assertEquals(result, groupDto);
		
		verify(userDao).find(USERNAME);
		verify(subscriptionGroupDao).find(user, GROUP_TITLE);
		verify(subscriptionGroupDao).getMaxOrder(user);
		verify(subscriptionGroupDao).save(group);
	}
	
	@Test
	public void createExistingGroup() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_TITLE)).thenReturn(group);
		
		try {
			service.create(USERNAME, GROUP_TITLE);
			fail();
		} catch(ServiceException e) {
			assertEquals(e.getStatus(), HttpStatus.CONFLICT);
		}
		
		verify(userDao).find(USERNAME);
		verify(subscriptionGroupDao).find(user, GROUP_TITLE);
		verify(subscriptionGroupDao, never()).save(group);
	}
	
	@Test
	public void deleteGroup() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		
		service.delete(USERNAME, GROUP_ID);
		
		verify(userDao).find(USERNAME);
		verify(subscriptionGroupDao).delete(group);
	}
	
	@Test
	public void entitleGroup() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.find(user, GROUP_ID)).thenReturn(group);
		
		service.entitle(USERNAME, GROUP_ID, GROUP_TITLE);
		
		verify(userDao).find(USERNAME);
		verify(group).setTitle(GROUP_TITLE);
		verify(subscriptionGroupDao).save(group);
	}
	
	@Test
	public void moveGroupUp() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));
		final long id = 100L;
		when(group.getId()).thenReturn(GROUP_ID);
		when(group1.getId()).thenReturn(id);
		when(group2.getId()).thenReturn(200L);
		
		when(group.getOrder()).thenReturn(1);
		when(group1.getOrder()).thenReturn(2);
		when(group2.getOrder()).thenReturn(3);
		
		service.moveUp(USERNAME, id);
		
		verify(group).setOrder(2);
		verify(group1).setOrder(1);
		verify(subscriptionGroupDao).saveAll(Arrays.asList(group, group1));
		verify(group2, never()).setOrder(anyInt());
	}
	
	@Test
	public void moveGroupDown() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));
		final long id = 100L;
		when(group.getId()).thenReturn(GROUP_ID);
		when(group1.getId()).thenReturn(id);
		when(group2.getId()).thenReturn(200L);
		
		when(group.getOrder()).thenReturn(1);
		when(group1.getOrder()).thenReturn(2);
		when(group2.getOrder()).thenReturn(3);
		
		service.moveDown(USERNAME, id);
		
		verify(group1).setOrder(3);
		verify(group2).setOrder(2);
		verify(subscriptionGroupDao).saveAll(Arrays.asList(group1, group2));
		verify(group, never()).setOrder(anyInt());
	}
	
	@Test
	public void list() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(subscriptionGroupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));
		when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription));
		when(subscriptionDao.list(group1)).thenReturn(Arrays.asList(subscription1, subscription2));
		when(subscriptionDao.list(group2)).thenReturn(Collections.<Subscription>emptyList());
		when(feedEntryDao.countUnread(subscription)).thenReturn(1);
		when(feedEntryDao.countUnread(subscription1)).thenReturn(2);
		when(feedEntryDao.countUnread(subscription2)).thenReturn(3);
		
		when(conversionService.convert(group, SubscriptionGroupDto.class)).thenReturn(groupDto);
		when(conversionService.convert(group1, SubscriptionGroupDto.class)).thenReturn(groupDto1);
		when(conversionService.convert(group2, SubscriptionGroupDto.class)).thenReturn(groupDto2);
		when(conversionService.convert(subscription, SubscriptionDto.class)).thenReturn(subscriptionDto);
		when(conversionService.convert(subscription1, SubscriptionDto.class)).thenReturn(subscriptionDto1);
		when(conversionService.convert(subscription2, SubscriptionDto.class)).thenReturn(subscriptionDto2);
		
		List<SubscriptionGroupDto> result = service.list(USERNAME);
		Assert.assertEquals(result, Arrays.asList(groupDto, groupDto1, groupDto2));
		
		verify(subscriptionDto).setUnreadCount(1);
		verify(subscriptionDto1).setUnreadCount(2);
		verify(subscriptionDto2).setUnreadCount(3);
		verify(groupDto).setUnreadCount(1);
		verify(groupDto1).setUnreadCount(5);
		verify(groupDto2).setUnreadCount(0);
	}

}
