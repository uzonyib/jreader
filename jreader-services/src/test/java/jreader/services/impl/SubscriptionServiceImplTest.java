package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.googlecode.objectify.Ref;

import jreader.dao.DaoFacade;
import jreader.dao.FeedDao;
import jreader.dao.GroupDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Role;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.RssFetchResult;
import jreader.dto.SubscriptionDto;
import jreader.services.RssService;
import jreader.services.exception.FetchException;
import jreader.services.exception.ResourceAlreadyExistsException;
import jreader.services.exception.ResourceNotFoundException;

public class SubscriptionServiceImplTest extends ServiceTest {

    private SubscriptionServiceImpl sut;

    @Mock
    private UserDao userDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private FeedDao feedDao;
    @Mock
    private PostDao postDao;
    @Mock
    private RssService rssService;
    @Mock
    private ConversionService conversionService;

    @Mock
    private Ref<Group> groupRef;
    @Mock
    private Ref<Feed> feedRef;
    @Mock
    private Post post1;
    @Mock
    private Post post2;

    private User user;
    private Feed feed;
    private Group group;
    private Subscription subscription;
    private Subscription subscription1;
    private Subscription subscription2;

    private RssFetchResult fetchResult;

    private SubscriptionDto subscriptionDto;
    
    @Captor
    private ArgumentCaptor<Subscription> subscriptionCaptor;
    @Captor
    private ArgumentCaptor<List<Subscription>> subscriptionListCaptor;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        user = new User("user", Role.USER, 1L);
        group = Group.builder().user(user).id(0L).title("group").order(1).build();

        feed = Feed.builder().title("feed_title").url("url").build();
        subscription = Subscription.builder().group(group).feed(feed).id(0L).title("subscription 0").order(1).build();
        subscription1 = Subscription.builder().group(group).id(1L).title("subscription 1").order(2).build();
        subscription2 = Subscription.builder().group(group).id(2L).title("subscription 2").order(3).build();

        fetchResult = new RssFetchResult(feed, Arrays.asList(post1, post2));
        subscriptionDto = new SubscriptionDto("0", "subscription", null, 1000L, 1);

        final DaoFacade daoFacade = DaoFacade.builder().userDao(userDao).groupDao(groupDao).subscriptionDao(subscriptionDao).feedDao(feedDao).postDao(postDao)
                .build();
        sut = new SubscriptionServiceImpl(daoFacade, rssService, conversionService);

        when(Ref.create(group)).thenReturn(groupRef);
        when(groupRef.get()).thenReturn(group);
        when(Ref.create(feed)).thenReturn(feedRef);
        when(feedRef.get()).thenReturn(feed);

        when(rssService.fetch(feed.getUrl())).thenReturn(fetchResult);

        when(userDao.find(user.getUsername())).thenReturn(user);
        when(groupDao.find(user, group.getId())).thenReturn(group);
        when(subscriptionDao.getMaxOrder(group)).thenReturn(subscription2.getOrder());
    }

    @Test
    public void subscribe_ShouldCreateNewSubscriptionWithoutPosts_IfFeedIsNew() {
        when(feedDao.find(feed.getUrl())).thenReturn(null);
        when(feedDao.save(feed)).thenReturn(feed);
        when(subscriptionDao.find(user, feed)).thenReturn(null);
        when(subscriptionDao.save(any(Subscription.class))).thenReturn(subscription);
        when(conversionService.convert(subscription, SubscriptionDto.class)).thenReturn(subscriptionDto);

        final SubscriptionDto actual = sut.subscribe(user.getUsername(), group.getId(), feed.getUrl());

        assertThat(actual).isEqualTo(subscriptionDto);

        verify(userDao).find(user.getUsername());
        verify(groupDao).find(user, group.getId());
        verify(rssService).fetch(feed.getUrl());
        verify(feedDao).find(feed.getUrl());
        verify(feedDao).save(feed);
        verify(subscriptionDao).find(user, feed);
        verify(subscriptionDao).save(subscriptionCaptor.capture());
        verifyZeroInteractions(postDao);
        
        assertThat(subscriptionCaptor.getValue().getGroup()).isEqualTo(group);
        assertThat(subscriptionCaptor.getValue().getFeed()).isEqualTo(feed);
        assertThat(subscriptionCaptor.getValue().getTitle()).isEqualTo(fetchResult.getFeed().getTitle());
        assertThat(subscriptionCaptor.getValue().getOrder()).isEqualTo(subscription2.getOrder() + 1);
        assertThat(subscriptionCaptor.getValue().getLastUpdateDate()).isNull();
    }

    @Test
    public void subscribe_ShouldCreateNewSubscriptionWithoutPosts_IfFeedExists() {
        when(feedDao.find(feed.getUrl())).thenReturn(feed);
        when(subscriptionDao.find(user, feed)).thenReturn(null);
        when(subscriptionDao.save(subscription)).thenReturn(subscription);

        sut.subscribe(user.getUsername(), group.getId(), feed.getUrl());

        verify(userDao).find(user.getUsername());
        verify(groupDao).find(user, group.getId());
        verifyZeroInteractions(rssService);
        verify(feedDao).find(feed.getUrl());
        verify(feedDao, never()).save(feed);
        verify(subscriptionDao).find(user, feed);
        verify(subscriptionDao).save(subscriptionCaptor.capture());
        verifyZeroInteractions(postDao);
        
        assertThat(subscriptionCaptor.getValue().getGroup()).isEqualTo(group);
        assertThat(subscriptionCaptor.getValue().getFeed()).isEqualTo(feed);
        assertThat(subscriptionCaptor.getValue().getTitle()).isEqualTo(fetchResult.getFeed().getTitle());
        assertThat(subscriptionCaptor.getValue().getOrder()).isEqualTo(subscription2.getOrder() + 1);
        assertThat(subscriptionCaptor.getValue().getLastUpdateDate()).isNull();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void subscribe_ShouldThrowException_IfFeedCannotBeFetched() {
        when(feedDao.find(feed.getUrl())).thenReturn(null);
        when(rssService.fetch(feed.getUrl())).thenThrow(new FetchException());

        sut.subscribe(user.getUsername(), group.getId(), feed.getUrl());
    }

    @Test(expectedExceptions = ResourceAlreadyExistsException.class)
    public void subscribe_ShouldThrowException_IfSubscriptionAlreadyExists() {
        when(feedDao.find(feed.getUrl())).thenReturn(feed);
        when(subscriptionDao.find(user, feed)).thenReturn(subscription);

        try {
            sut.subscribe(user.getUsername(), group.getId(), feed.getUrl());
            fail();
        } finally {
            verify(userDao).find(user.getUsername());
            verifyZeroInteractions(groupDao);
            verifyZeroInteractions(rssService);
            verify(feedDao).find(feed.getUrl());
            verify(feedDao, never()).save(feed);
            verify(subscriptionDao).find(user, feed);
            verify(subscriptionDao, never()).save(subscription);
            verifyZeroInteractions(postDao);
        }
    }

    @Test
    public void unsubscribe_ShouldDeleteSubscriptionWithPosts() {
        when(subscriptionDao.find(group, subscription.getId())).thenReturn(subscription);
        final List<Post> posts = Arrays.asList(post1, post2);
        when(postDao.list(subscription)).thenReturn(posts);

        sut.unsubscribe(user.getUsername(), group.getId(), subscription.getId());

        verify(postDao).deleteAll(posts);
        verify(subscriptionDao).delete(subscription);
    }

    @Test
    public void entitle_ShouldUpdateSubscriptionTitle() {
        when(subscriptionDao.find(group, subscription.getId())).thenReturn(subscription);
        final String newTitle = "new title";

        sut.entitle(user.getUsername(), group.getId(), subscription.getId(), newTitle);

        verify(subscriptionDao).save(subscriptionCaptor.capture());
        assertThat(subscriptionCaptor.getValue().getTitle()).isEqualTo(newTitle);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, dataProviderClass = ServiceDataProviders.class, dataProvider = "invalidSubscriptionTitles")
    public void entitle_ShouldThrowExceptionForInvalidTitle(String title) {
        sut.entitle(user.getUsername(), group.getId(), subscription.getId(), title);
    }

    @Test(expectedExceptions = ResourceAlreadyExistsException.class)
    public void entitle_ShouldThrowException_IfSubscriptionAlreadyExists() {
        final String newTitle = "new title";
        when(subscriptionDao.find(group, newTitle)).thenReturn(subscription);

        sut.entitle(user.getUsername(), group.getId(), subscription.getId(), newTitle);
    }

    @Test
    public void moveUp_ShouldUpdateSubscriptionOrders() {
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription, subscription1, subscription2));

        sut.moveUp(user.getUsername(), group.getId(), subscription1.getId());

        verify(subscriptionDao).saveAll(subscriptionListCaptor.capture());
        assertThat(subscriptionListCaptor.getValue().get(0).getId()).isEqualTo(subscription.getId());
        assertThat(subscriptionListCaptor.getValue().get(0).getOrder()).isEqualTo(2);
        assertThat(subscriptionListCaptor.getValue().get(1).getId()).isEqualTo(subscription1.getId());
        assertThat(subscriptionListCaptor.getValue().get(1).getOrder()).isEqualTo(1);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void moveUp_ShouldThrowException_WhenSubscriptionNotFound() {
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription, subscription1, subscription2));

        sut.moveUp(user.getUsername(), group.getId(), subscription2.getId() + 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void moveUp_ShouldThrowException_WhenMovingFirstSubscription() {
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription, subscription1, subscription2));

        sut.moveUp(user.getUsername(), group.getId(), subscription.getId());
    }

    @Test
    public void moveDown_ShouldUpdateSubscriptionOrders() {
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription, subscription1, subscription2));

        sut.moveDown(user.getUsername(), group.getId(), subscription1.getId());

        verify(subscriptionDao).saveAll(subscriptionListCaptor.capture());
        assertThat(subscriptionListCaptor.getValue().get(0).getId()).isEqualTo(subscription1.getId());
        assertThat(subscriptionListCaptor.getValue().get(0).getOrder()).isEqualTo(3);
        assertThat(subscriptionListCaptor.getValue().get(1).getId()).isEqualTo(subscription2.getId());
        assertThat(subscriptionListCaptor.getValue().get(1).getOrder()).isEqualTo(2);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void moveDown_ShouldThrowException_WhenSubscriptionNotFound() {
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription, subscription1, subscription2));

        sut.moveDown(user.getUsername(), group.getId(), subscription2.getId() + 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void moveDown_ShouldThrowException_WhenMovingLastSubscription() {
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription, subscription1, subscription2));

        sut.moveDown(user.getUsername(), group.getId(), subscription2.getId());
    }

}
