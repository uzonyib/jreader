package jreader.services.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.GroupDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.PostDto;
import jreader.services.PostFilter;
import jreader.services.PostFilter.Vertical;

public class PostServiceImplTest {
	
	private static final String USERNAME = "user";
	private static final Long GROUP_ID_1 = 1L;
	private static final Long GROUP_ID_2 = 2L;
	private static final Long SUBSCRIPTION_ID_1 = 11L;
	private static final Long SUBSCRIPTION_ID_2 = 12L;
	private static final Long POST_ID_1 = 21L;
	private static final Long POST_ID_2 = 22L;
	
	@InjectMocks
	private PostServiceImpl service;

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
	private User user;
	@Mock
	private Group group1;
	@Mock
	private Group group2;
	@Mock
	private Subscription subscription1;
	@Mock
	private Subscription subscription2;
	@Mock
	private Post post1;
	@Mock
	private Post post2;
	@Mock
	private PostFilter filter;
	@Mock
	private PostDto postDto1;
	@Mock
	private PostDto postDto2;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void markRead() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(groupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(groupDao.find(user, GROUP_ID_2)).thenReturn(group2);
		when(subscriptionDao.find(group1, SUBSCRIPTION_ID_1)).thenReturn(subscription1);
		when(subscriptionDao.find(group2, SUBSCRIPTION_ID_2)).thenReturn(subscription2);
		when(postDao.find(subscription1, POST_ID_1)).thenReturn(post1);
		when(postDao.find(subscription2, POST_ID_2)).thenReturn(post2);
		when(post1.isRead()).thenReturn(false);
		when(post2.isRead()).thenReturn(false);
		
		Map<Long, List<Long>> subscription1Ids = new HashMap<Long, List<Long>>();
		subscription1Ids.put(SUBSCRIPTION_ID_1, Arrays.asList(POST_ID_1));
		Map<Long, List<Long>> subscription2Ids = new HashMap<Long, List<Long>>();
		subscription2Ids.put(SUBSCRIPTION_ID_2, Arrays.asList(POST_ID_2));
		Map<Long, Map<Long, List<Long>>> ids = new HashMap<Long, Map<Long,List<Long>>>();
		ids.put(GROUP_ID_1, subscription1Ids);
		ids.put(GROUP_ID_2, subscription2Ids);
		service.markRead(USERNAME, ids);
		
		verify(post1).setRead(true);
		verify(post2).setRead(true);
		verify(postDao).saveAll(Arrays.asList(post1, post2));
	}
	
	@Test
	public void bookmark() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(groupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(subscriptionDao.find(group1, SUBSCRIPTION_ID_1)).thenReturn(subscription1);
		when(postDao.find(subscription1, POST_ID_1)).thenReturn(post1);
		when(post1.isBookMarked()).thenReturn(false);
		
		service.bookmark(USERNAME, GROUP_ID_1, SUBSCRIPTION_ID_1, POST_ID_1);
		
		verify(post1).setBookmarked(true);
		verify(postDao).save(post1);
	}
	
	@Test
	public void deleteBookmark() {
		when(userDao.find(USERNAME)).thenReturn(user);
		when(groupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(subscriptionDao.find(group1, SUBSCRIPTION_ID_1)).thenReturn(subscription1);
		when(postDao.find(subscription1, POST_ID_1)).thenReturn(post1);
		when(post1.isBookMarked()).thenReturn(true);
		
		service.deleteBookmark(USERNAME, GROUP_ID_1, SUBSCRIPTION_ID_1, POST_ID_1);
		
		verify(post1).setBookmarked(false);
		verify(postDao).save(post1);
	}
	
	@Test
	public void listForUser() {
		when(filter.getVertical()).thenReturn(Vertical.ALL);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(userDao.find(USERNAME)).thenReturn(user);
		List<Post> entities = Arrays.asList(post1, post2);
        when(postDao.list(user, filter)).thenReturn(entities);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Post.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostDto.class)))).thenReturn(Arrays.asList(postDto1, postDto2));
		
		List<PostDto> result = service.list(filter);
		
		verify(postDao).list(user, filter);
		Assert.assertEquals(result, Arrays.asList(postDto1, postDto2));
	}
	
	@Test
	public void listForGroup() {
		when(filter.getVertical()).thenReturn(Vertical.GROUP);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(filter.getGroupId()).thenReturn(GROUP_ID_1);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(groupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		List<Post> entities = Arrays.asList(post1, post2);
        when(postDao.list(group1, filter)).thenReturn(entities);
		when(conversionService.convert(entities,
		        TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Post.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostDto.class)))).thenReturn(Arrays.asList(postDto1, postDto2));
		
		List<PostDto> result = service.list(filter);
		
		verify(postDao).list(group1, filter);
		Assert.assertEquals(result, Arrays.asList(postDto1, postDto2));
	}
	
	@Test
	public void listForSubscription() {
		when(filter.getVertical()).thenReturn(Vertical.SUBSCRIPTION);
		when(filter.getUsername()).thenReturn(USERNAME);
		when(filter.getGroupId()).thenReturn(GROUP_ID_1);
		when(filter.getSubscriptionId()).thenReturn(SUBSCRIPTION_ID_1);
		when(userDao.find(USERNAME)).thenReturn(user);
		when(groupDao.find(user, GROUP_ID_1)).thenReturn(group1);
		when(subscriptionDao.find(group1, SUBSCRIPTION_ID_1)).thenReturn(subscription1);
		List<Post> entities = Arrays.asList(post1, post2);
        when(postDao.list(subscription1, filter)).thenReturn(entities);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Post.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostDto.class)))).thenReturn(Arrays.asList(postDto1, postDto2));
		
		List<PostDto> result = service.list(filter);
		
		verify(postDao).list(subscription1, filter);
		Assert.assertEquals(result, Arrays.asList(postDto1, postDto2));
	}
	
}
