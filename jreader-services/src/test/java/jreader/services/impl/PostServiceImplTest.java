package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.DaoFacade;
import jreader.dao.GroupDao;
import jreader.dao.PostDao;
import jreader.dao.PostFilter.PostType;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Role;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.PostDto;
import jreader.services.PostFilter;

public class PostServiceImplTest extends ServiceTest {

    private static final Long POST_1_ID = 21L;
    private static final Long POST_2_ID = 22L;

    private PostServiceImpl sut;

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

    private User user;
    private Group group1;
    private Group group2;
    private Subscription subscription1;
    private Subscription subscription2;

    @Mock
    private Post post1;
    @Mock
    private Post post2;

    private PostDto postDto1;
    private PostDto postDto2;
    
    private List<Post> postEntities;
    private List<PostDto> postDtos;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        user = new User("user", Role.USER, 1L);

        group1 = Group.builder().user(user).id(1L).title("group 1").order(2).build();
        group2 = Group.builder().user(user).id(2L).title("group 2").order(3).build();

        subscription1 = Subscription.builder().group(group1).id(1L).title("subscription 1").build();
        subscription2 = Subscription.builder().group(group2).id(2L).title("subscription 2").build();

        postDto1 = PostDto.builder().id("0").build();
        postDto2 = PostDto.builder().id("1").build();
        
        postEntities = Arrays.asList(post1, post2);
        postDtos = Arrays.asList(postDto1, postDto2);

        final DaoFacade daoFacade = DaoFacade.builder().userDao(userDao).groupDao(groupDao).subscriptionDao(subscriptionDao).postDao(postDao).build();
        sut = new PostServiceImpl(daoFacade, conversionService);

        when(userDao.find(user.getUsername())).thenReturn(user);

        when(groupDao.find(user, group1.getId())).thenReturn(group1);
        when(groupDao.find(user, group2.getId())).thenReturn(group2);

        when(subscriptionDao.find(group1, subscription1.getId())).thenReturn(subscription1);
        when(subscriptionDao.find(group2, subscription2.getId())).thenReturn(subscription2);

        when(postDao.find(subscription1, POST_1_ID)).thenReturn(post1);
        when(postDao.find(subscription2, POST_2_ID)).thenReturn(post2);
    }

    @Test
    public void markRead_ShouldMarkPostsReadAndSave() {
        when(post1.isRead()).thenReturn(false);
        when(post2.isRead()).thenReturn(false);

        final Map<Long, List<Long>> subscription1Ids = new HashMap<Long, List<Long>>();
        subscription1Ids.put(subscription1.getId(), Arrays.asList(POST_1_ID));
        final Map<Long, List<Long>> subscription2Ids = new HashMap<Long, List<Long>>();
        subscription2Ids.put(subscription2.getId(), Arrays.asList(POST_2_ID));
        final Map<Long, Map<Long, List<Long>>> ids = new HashMap<Long, Map<Long, List<Long>>>();
        ids.put(group1.getId(), subscription1Ids);
        ids.put(group2.getId(), subscription2Ids);

        sut.markRead(user.getUsername(), ids);

        verify(post1).setRead(true);
        verify(post2).setRead(true);
        verify(postDao).saveAll(Arrays.asList(post1, post2));
    }

    @Test
    public void bookmark_ShouldBookmarkPostAndSave() {
        when(post1.isBookmarked()).thenReturn(false);

        sut.bookmark(user.getUsername(), group1.getId(), subscription1.getId(), POST_1_ID);

        verify(post1).setBookmarked(true);
        verify(postDao).save(post1);
    }

    @Test
    public void deleteBookmark_ShouldClearBookmarkedFlagAndSavePost() {
        when(post1.isBookmarked()).thenReturn(true);

        sut.deleteBookmark(user.getUsername(), group1.getId(), subscription1.getId(), POST_1_ID);

        verify(post1).setBookmarked(false);
        verify(postDao).save(post1);
    }

    @Test
    public void list_ShouldReturnPostsForUser() {
        final PostFilter filter = new PostFilter(user.getUsername(), PostType.ALL, true, 0, 10);
        when(postDao.list(user, filter.getEntityFilter())).thenReturn(postEntities);
        when(conversionService.convert(postEntities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Post.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostDto.class)))).thenReturn(postDtos);

        final List<PostDto> actual = sut.list(filter);

        verify(postDao).list(user, filter.getEntityFilter());
        assertThat(actual).isEqualTo(postDtos);
    }

    @Test
    public void list_ShouldReturnPostsForGroup() {
        final PostFilter filter = new PostFilter(user.getUsername(), group1.getId(), PostType.ALL, true, 0, 10);
        when(postDao.list(group1, filter.getEntityFilter())).thenReturn(postEntities);
        when(conversionService.convert(postEntities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Post.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostDto.class)))).thenReturn(postDtos);

        final List<PostDto> actual = sut.list(filter);

        verify(postDao).list(group1, filter.getEntityFilter());
        assertThat(actual).isEqualTo(postDtos);
    }

    @Test
    public void list_ShouldReturnPostsForSubscription() {
        final PostFilter filter = new PostFilter(user.getUsername(), group1.getId(), subscription1.getId(), PostType.ALL, true, 0, 10);
        when(postDao.list(subscription1, filter.getEntityFilter())).thenReturn(postEntities);
        when(conversionService.convert(postEntities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Post.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(PostDto.class)))).thenReturn(postDtos);

        final List<PostDto> actual = sut.list(filter);

        verify(postDao).list(subscription1, filter.getEntityFilter());
        assertThat(actual).isEqualTo(postDtos);
    }

}
