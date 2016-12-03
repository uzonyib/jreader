package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.DaoFacade;
import jreader.dao.GroupDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Group;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.GroupDto;
import jreader.dto.SubscriptionDto;
import jreader.services.ServiceException;

public class GroupServiceImplTest {
	
	private static final String USERNAME = "user";
	private static final String GROUP_TITLE = "group_title";
	private static final int GROUP_ORDER = 10;
	private static final long GROUP_ID = 123;
	
	private GroupServiceImpl service;

	@Mock
	private UserDao userDao;
	@Mock
	private GroupDao groupDao;
	@Mock
	private SubscriptionDao subscriptionDao;
	@Mock
	private PostDao postDao;
	@Mock
	private ConversionService conversionService;
	@Mock
	private BuilderFactory builderFactory;
	@Mock
	private Group.Builder groupBuilder;
	
	@Mock
	private User user;
	@Mock
	private Group group;
	@Mock
	private Group group1;
	@Mock
	private Group group2;
	@Mock
	private Subscription subscription;
	@Mock
	private Subscription subscription1;
	@Mock
	private Subscription subscription2;
	
	private GroupDto groupDto;
	private GroupDto groupDto1;
	private GroupDto groupDto2;
	
	private SubscriptionDto subscriptionDto;
	private SubscriptionDto subscriptionDto1;
	private SubscriptionDto subscriptionDto2;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		groupDto = new GroupDto("0", "group", 1);
		groupDto1 = new GroupDto("1", "group1", 2);
		groupDto2 = new GroupDto("2", "group2", 3);
		
		subscriptionDto = new SubscriptionDto("0", "subscription", null, 1000L, 1);
		subscriptionDto1 = new SubscriptionDto("1", "subscription1", null, 2000L, 2);
		subscriptionDto2 = new SubscriptionDto("2", "subscription2", null, 3000L, 3);
		
        DaoFacade daoFacade = DaoFacade.builder().userDao(userDao).groupDao(groupDao).subscriptionDao(subscriptionDao).postDao(postDao).build();
        service = new GroupServiceImpl(daoFacade, conversionService, builderFactory);
        
        when(userDao.find(USERNAME)).thenReturn(user);
	}
	
	@Test
	public void createNewGroup() {
		when(groupDao.find(user, GROUP_TITLE)).thenReturn(null);
		when(groupDao.getMaxOrder(user)).thenReturn(GROUP_ORDER - 1);
		when(builderFactory.createGroupBuilder()).thenReturn(groupBuilder);
		when(groupBuilder.user(user)).thenReturn(groupBuilder);
		when(groupBuilder.title(GROUP_TITLE)).thenReturn(groupBuilder);
		when(groupBuilder.order(GROUP_ORDER)).thenReturn(groupBuilder);
		when(groupBuilder.build()).thenReturn(group);
		when(groupDao.save(group)).thenReturn(group);
		when(conversionService.convert(group, GroupDto.class)).thenReturn(groupDto);
		
		GroupDto result = service.create(USERNAME, GROUP_TITLE);
		assertEquals(result, groupDto);
		
		verify(userDao).find(USERNAME);
		verify(groupDao).find(user, GROUP_TITLE);
		verify(groupDao).getMaxOrder(user);
		verify(groupDao).save(group);
	}
	
	@Test
	public void createExistingGroup() {
		when(groupDao.find(user, GROUP_TITLE)).thenReturn(group);
		
		try {
			service.create(USERNAME, GROUP_TITLE);
			fail();
		} catch(ServiceException e) {
			assertEquals(e.getStatus(), HttpStatus.CONFLICT);
		}
		
		verify(userDao).find(USERNAME);
		verify(groupDao).find(user, GROUP_TITLE);
		verify(groupDao, never()).save(group);
	}
	
	@Test
	public void deleteGroup() {
		when(groupDao.find(user, GROUP_ID)).thenReturn(group);
		
		service.delete(USERNAME, GROUP_ID);
		
		verify(userDao).find(USERNAME);
		verify(groupDao).delete(group);
	}
	
	@Test
	public void entitleGroup() {
		when(groupDao.find(user, GROUP_ID)).thenReturn(group);
		
		service.entitle(USERNAME, GROUP_ID, GROUP_TITLE);
		
		verify(userDao).find(USERNAME);
		verify(group).setTitle(GROUP_TITLE);
		verify(groupDao).save(group);
	}
	
	@Test
	public void moveGroupUp() {
		when(groupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));
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
		verify(groupDao).saveAll(Arrays.asList(group, group1));
		verify(group2, never()).setOrder(anyInt());
	}
	
	@Test
	public void moveGroupDown() {
		when(groupDao.list(user)).thenReturn(Arrays.asList(group, group1, group2));
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
		verify(groupDao).saveAll(Arrays.asList(group1, group2));
		verify(group, never()).setOrder(anyInt());
	}
	
	@Test
	public void list() {
		List<Group> groups = Arrays.asList(group, group1, group2);
        when(groupDao.list(user)).thenReturn(groups);
		List<Subscription> subscriptions = Arrays.asList(subscription);
        when(subscriptionDao.list(group)).thenReturn(subscriptions);
		List<Subscription> subscriptions1 = Arrays.asList(subscription1, subscription2);
        when(subscriptionDao.list(group1)).thenReturn(subscriptions1);
		List<Subscription> subscriptions2 = Collections.emptyList();
        when(subscriptionDao.list(group2)).thenReturn(subscriptions2);
		when(postDao.countUnread(subscription)).thenReturn(1);
		when(postDao.countUnread(subscription1)).thenReturn(2);
		when(postDao.countUnread(subscription2)).thenReturn(3);
		
		when(conversionService.convert(groups,
		        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Group.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(GroupDto.class)))).thenReturn(Arrays.asList(groupDto, groupDto1, groupDto2));
		
		List<SubscriptionDto> dtos = Arrays.asList(subscriptionDto);
        when(conversionService.convert(subscriptions,
		        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Subscription.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(SubscriptionDto.class)))).thenReturn(dtos);
		List<SubscriptionDto> dtos1 = Arrays.asList(subscriptionDto1, subscriptionDto2);
        when(conversionService.convert(subscriptions1,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Subscription.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(SubscriptionDto.class))))
		    .thenReturn(dtos1);
		List<SubscriptionDto> dtos2 = Collections.emptyList();
        when(conversionService.convert(subscriptions2,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Subscription.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(SubscriptionDto.class)))).thenReturn(dtos2);
		
		List<GroupDto> result = service.list(USERNAME);
		Assert.assertEquals(result, Arrays.asList(groupDto, groupDto1, groupDto2));
		
		assertSame(groupDto.getSubscriptions(), dtos);
		assertSame(groupDto1.getSubscriptions(), dtos1);
		assertSame(groupDto2.getSubscriptions(), dtos2);
		
		assertEquals(groupDto.getUnreadCount(), 1);
		assertEquals(groupDto1.getUnreadCount(), 5);
		assertEquals(groupDto2.getUnreadCount(), 0);
		
		assertEquals(subscriptionDto.getUnreadCount(), 1);
		assertEquals(subscriptionDto1.getUnreadCount(), 2);
		assertEquals(subscriptionDto2.getUnreadCount(), 3);
	}

}
