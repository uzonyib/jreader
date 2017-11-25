package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.googlecode.objectify.Ref;

import jreader.dao.DaoFacade;
import jreader.dao.FeedDao;
import jreader.dao.FeedStatDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Role;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.FeedDto;
import jreader.dto.RssFetchResult;
import jreader.services.DateHelper;
import jreader.services.RssService;
import jreader.services.exception.FetchException;

public class CronServiceImplTest extends ServiceTest {

    private CronServiceImpl sut;

    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private FeedDao feedDao;
    @Mock
    private PostDao postDao;
    @Mock
    private FeedStatDao feedStatDao;

    @Mock
    private RssService rssService;
    @Mock
    private ConversionService conversionService;

    @Mock
    private DateHelper dateHelper;

    @Mock
    private Subscription subscription1;
    @Mock
    private Subscription subscription2;

    @Mock
    private Post post11;
    @Mock
    private Post post12;
    @Mock
    private Post post13;
    @Mock
    private Post post21;
    @Mock
    private Post post22;

    @Mock
    private List<FeedStat> feedStats;

    @Mock
    private Ref<User> userRef;
    @Mock
    private Ref<Group> groupRef;
    @Mock
    private Ref<Feed> feedRef;

    private User user;
    private Feed feed1;
    private Feed feed2;
    private RssFetchResult fetchResult;
    private Group group;

    private FeedDto dto1;
    private FeedDto dto2;

    @Captor
    private ArgumentCaptor<Feed> feedCaptor;
    @Captor
    private ArgumentCaptor<List<FeedStat>> feedStatListCaptor;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        user = new User("user", Role.USER, 1L);
        when(Ref.create(user)).thenReturn(userRef);
        when(userRef.get()).thenReturn(user);

        feed1 = Feed.builder().title("feed title 1").url("url1").build();
        when(Ref.create(feed1)).thenReturn(feedRef);
        when(feedRef.get()).thenReturn(feed1);
        feed2 = Feed.builder().title("feed title 2").url("url2").build();
        fetchResult = new RssFetchResult(null, Arrays.asList(post11, post12, post13));

        group = Group.builder().user(user).id(0L).title("group").order(1).build();
        when(Ref.create(group)).thenReturn(groupRef);
        when(groupRef.get()).thenReturn(group);

        dto1 = FeedDto.builder().title("feed1").build();
        dto2 = FeedDto.builder().title("feed2").build();

        final DaoFacade daoFacade = DaoFacade.builder().subscriptionDao(subscriptionDao).feedDao(feedDao).postDao(postDao).feedStatDao(feedStatDao).build();
        sut = new CronServiceImpl(daoFacade, rssService, conversionService, dateHelper);

        when(feedDao.find(feed1.getUrl())).thenReturn(feed1);
        when(feedDao.find(feed2.getUrl())).thenReturn(feed2);
    }

    @Test
    public void list_ShouldReturnFeedDtos() {
        final List<Feed> entities = Arrays.asList(feed1, feed2);
        when(feedDao.listAll()).thenReturn(entities);
        final List<FeedDto> expected = Arrays.asList(dto1, dto2);
        when(conversionService.convert(entities, TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Feed.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedDto.class)))).thenReturn(expected);

        final List<FeedDto> actual = sut.listFeeds();

        verify(feedDao).listAll();

        verify(conversionService).convert(entities, TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Feed.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedDto.class)));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void refresh_ShouldSaveNewPosts() {
        feed1.setStatus(null);

        final long date = 1445271922000L;
        when(dateHelper.getCurrentDate()).thenReturn(date);
        final long lastUpdateDate = date - 1000 * 60 * 20;
        feed1.setLastUpdateDate(lastUpdateDate);

        when(rssService.fetch(feed1.getUrl())).thenReturn(fetchResult);

        when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1, subscription2));

        when(subscription1.getLastUpdateDate()).thenReturn(lastUpdateDate);
        when(subscription2.getLastUpdateDate()).thenReturn(lastUpdateDate);

        final DateHelper dh = new DateHelperImpl();
        final long pubDate1 = date - 1000 * 60 * 30;
        when(post11.getPublishDate()).thenReturn(pubDate1);
        when(post11.getUri()).thenReturn("uri1");
        final long day = dh.getFirstSecondOfDay(pubDate1);
        when(dateHelper.getFirstSecondOfDay(pubDate1)).thenReturn(day);

        final long pubDate2 = date - 1000 * 60 * 10;
        when(post12.getPublishDate()).thenReturn(pubDate2);
        when(post12.getUri()).thenReturn("uri2");
        when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(dh.getFirstSecondOfDay(pubDate2));

        when(post13.getPublishDate()).thenReturn(pubDate2);
        when(post13.getUri()).thenReturn("uri3");

        when(subscription1.getGroup()).thenReturn(group);
        when(subscription2.getGroup()).thenReturn(group);

        sut.refresh(feed1.getUrl());

        verify(rssService).fetch(feed1.getUrl());

        verify(subscriptionDao).listSubscriptions(feed1);

        verify(postDao, never()).save(post11);

        verify(post12).setSubscription(subscription1);
        verify(post12).setSubscription(subscription2);
        verify(postDao, times(2)).save(post12);

        verify(post13).setSubscription(subscription1);
        verify(post13).setSubscription(subscription2);
        verify(postDao, times(2)).save(post13);

        verify(subscription1).setLastUpdateDate(pubDate2);
        verify(subscriptionDao).save(subscription1);
        verify(subscription2).setLastUpdateDate(pubDate2);
        verify(subscriptionDao).save(subscription2);

        verify(feedDao).save(feedCaptor.capture());
        assertThat(feedCaptor.getValue().getLastRefreshDate()).isEqualTo(date);
        assertThat(feedCaptor.getValue().getStatus()).isEqualTo(0);

        verify(feedStatDao).saveAll(feedStatListCaptor.capture());
        assertThat(feedStatListCaptor.getValue()).isEqualTo(Arrays.asList(FeedStat.builder().feed(feed1).refreshDate(day).count(2).build()));
    }

    @Test
    public void refresh_ShouldNotSavePost_IfNewPostIsInTheFuture() {
        final long date = 1445271922000L;
        final long lastRefreshDate = date - 1000 * 60 * 20;
        feed1.setLastRefreshDate(lastRefreshDate);
        when(dateHelper.getCurrentDate()).thenReturn(date);

        when(rssService.fetch(feed1.getUrl())).thenReturn(new RssFetchResult(null, Arrays.asList(post11)));

        when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1));

        when(subscription1.getLastUpdateDate()).thenReturn(lastRefreshDate);

        final long pubDate1 = date + 1000 * 60 * 30;
        when(post11.getPublishDate()).thenReturn(pubDate1);
        when(post11.getUri()).thenReturn("uri1");
        final long day = new DateHelperImpl().getFirstSecondOfDay(pubDate1);
        when(dateHelper.getFirstSecondOfDay(pubDate1)).thenReturn(day);

        when(subscription1.getGroup()).thenReturn(group);

        sut.refresh(feed1.getUrl());

        verify(rssService).fetch(feed1.getUrl());
        verify(subscriptionDao).listSubscriptions(feed1);
        verifyNoMoreInteractions(postDao);
    }

    @Test
    public void refreshFeeds_ShouldSetStatusToOne_IfStatusIsNotSetAndRefreshFails() {
        when(rssService.fetch(feed1.getUrl())).thenThrow(new FetchException());

        sut.refresh(feed1.getUrl());

        verify(rssService).fetch(feed1.getUrl());
        verify(feedDao).save(feedCaptor.capture());
        assertThat(feedCaptor.getValue().getStatus()).isEqualTo(1);
    }

    @Test
    public void refreshFeeds_ShouldIncrementStatus_IfRefreshFails() {
        feed1.setStatus(3);
        when(rssService.fetch(feed1.getUrl())).thenThrow(new FetchException());

        sut.refresh(feed1.getUrl());

        verify(rssService).fetch(feed1.getUrl());
        verify(feedDao).save(feedCaptor.capture());
        assertThat(feedCaptor.getValue().getStatus()).isEqualTo(4);
    }

    @Test
    public void refreshFeeds_ShouldKeepMaxStatus_IfStatusIsMaxAndRefreshFails() {
        feed1.setStatus(5);
        when(rssService.fetch(feed1.getUrl())).thenThrow(new FetchException());

        sut.refresh(feed1.getUrl());

        verify(rssService).fetch(feed1.getUrl());
        verify(feedDao).save(feedCaptor.capture());
        assertThat(feedCaptor.getValue().getStatus()).isEqualTo(5);
    }

    @Test
    public void isNew_ShouldReturnFalse_IfPublishDateIsNull() {
        when(post11.getPublishDate()).thenReturn(null);

        final boolean isNew = sut.isNew(post11, feed1, 1100L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNew_ShouldReturnFalse_IfUriIsNull() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn(null);

        final boolean isNew = sut.isNew(post11, feed1, 1100L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNew_ShouldReturnFalse_IfPublishDateIsInFuture() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");

        final boolean isNew = sut.isNew(post11, feed1, 999L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNew_ShouldReturnFalse_IfFeedLastUpdateDateIsLargerThanRefreshDate() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        feed1.setLastUpdateDate(1100L);

        final boolean isNew = sut.isNew(post11, feed1, 1000L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNew_ShouldReturnTrue_IfFeedLastUpdateDateIsNull() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        feed1.setLastUpdateDate(null);

        final boolean isNew = sut.isNew(post11, feed1, 1100L);

        assertThat(isNew).isTrue();
    }

    @Test
    public void isNew_ShouldReturnFalse_IfPublishDateIsEqualToLastUpdateDate() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        feed1.setLastUpdateDate(1000L);

        final boolean isNew = sut.isNew(post11, feed1, 1100L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNew_ShouldReturnTrue_IfPublishDateIsLargerThanUpdatedDate() {
        when(post11.getPublishDate()).thenReturn(1001L);
        when(post11.getUri()).thenReturn("uri");
        feed1.setLastUpdateDate(1000L);

        final boolean isNew = sut.isNew(post11, feed1, 1100L);

        assertThat(isNew).isTrue();
    }

    @Test
    public void isNewSubscription_ShouldReturnFalse_IfPublishDateIsNull() {
        when(post11.getPublishDate()).thenReturn(null);

        final boolean isNew = sut.isNew(post11, subscription1, 1100L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNewSubscription_ShouldReturnFalse_IfUriIsNull() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn(null);

        final boolean isNew = sut.isNew(post11, subscription1, 1100L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNewSubscription_ShouldReturnFalse_IfPublishDateIsInFuture() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");

        final boolean isNew = sut.isNew(post11, subscription1, 999L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNewSubscription_ShouldReturnFalse_IfPublishDateIsSmallerThanRefreshDate() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(subscription1.getLastUpdateDate()).thenReturn(1100L);

        final boolean isNew = sut.isNew(post11, subscription1, 1000L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNewSubscription_ShouldReturnTrue_IfPublishDateIsEqualToLastUpdateDate() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(subscription1.getLastUpdateDate()).thenReturn(1000L);
        when(postDao.find(subscription1, "uri", 1000L)).thenReturn(null);

        final boolean isNew = sut.isNew(post11, subscription1, 1100L);

        assertThat(isNew).isTrue();
    }

    @Test
    public void isNewSubscription_ShouldReturnFalse_IfPostFound() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(subscription1.getLastUpdateDate()).thenReturn(900L);
        when(postDao.find(subscription1, "uri", 1000L)).thenReturn(post11);

        final boolean isNew = sut.isNew(post11, subscription1, 1100L);

        assertThat(isNew).isFalse();
    }

    @Test
    public void isNewSubscription_ShouldReturnTrue_IfPostNotFound() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(subscription1.getLastUpdateDate()).thenReturn(900L);
        when(postDao.find(subscription1, "uri", 1000L)).thenReturn(null);

        final boolean isNew = sut.isNew(post11, subscription1, 1100L);

        assertThat(isNew).isTrue();
    }

    @Test
    public void updateFeed_ShouldUpdateStatAttributes_IfStatNotYetExistsForTheDay() {
        feed1.setLastUpdateDate(850L);

        final long currentDate = 1000L;

        final long pubDate1 = 700L;
        when(post11.getPublishDate()).thenReturn(pubDate1);
        when(post11.getUri()).thenReturn("uri1");

        final long pubDate2 = 900L;
        when(post12.getPublishDate()).thenReturn(pubDate2);
        when(post12.getUri()).thenReturn("uri2");
        final long day = 800L;
        when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(day);

        final long pubDate3 = 900L;
        when(post13.getPublishDate()).thenReturn(pubDate3);
        when(post13.getUri()).thenReturn("uri3");
        when(dateHelper.getFirstSecondOfDay(pubDate3)).thenReturn(day);

        sut.updateFeed(feed1, currentDate, fetchResult);

        verify(feedDao).save(feedCaptor.capture());
        assertThat(feedCaptor.getValue().getLastRefreshDate()).isEqualTo(currentDate);

        verify(feedStatDao).saveAll(feedStatListCaptor.capture());
        assertThat(feedStatListCaptor.getValue()).isEqualTo(Arrays.asList(FeedStat.builder().feed(feed1).refreshDate(day).count(2).build()));
    }

    @Test
    public void updateFeed_ShouldUpdateStatAttributes_IsStatAlreadyExistsForTheDay() {
        feed1.setLastUpdateDate(850L);

        final long currentDate = 1000L;

        final long pubDate1 = 700L;
        when(post11.getPublishDate()).thenReturn(pubDate1);
        when(post11.getUri()).thenReturn("uri1");

        final long pubDate2 = 900L;
        when(post12.getPublishDate()).thenReturn(pubDate2);
        when(post12.getUri()).thenReturn("uri2");
        final long day2 = 800L;
        when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(day2);

        final long pubDate3 = 950L;
        when(post13.getPublishDate()).thenReturn(pubDate3);
        when(post13.getUri()).thenReturn("uri3");
        final long day3 = 900L;
        when(dateHelper.getFirstSecondOfDay(pubDate3)).thenReturn(day3);

        final FeedStat stat = FeedStat.builder().feed(feed1).refreshDate(day2).count(5).build();
        when(feedStatDao.find(feed1, day2)).thenReturn(stat);

        sut.updateFeed(feed1, currentDate, fetchResult);

        verify(feedDao).save(feedCaptor.capture());
        assertThat(feedCaptor.getValue().getLastRefreshDate()).isEqualTo(currentDate);

        verify(feedStatDao).saveAll(feedStatListCaptor.capture());
        assertThat(feedStatListCaptor.getValue()).isEqualTo(Arrays.asList(
                FeedStat.builder().feed(feed1).refreshDate(day2).count(6).build(),
                FeedStat.builder().feed(feed1).refreshDate(day3).count(1).build()));
    }

    @Test
    public void cleanup_ShouldDeleteOnlyPostsAndStats_IfSubscriptionExistsToFeed() {
        when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1, subscription2));

        when(postDao.find(subscription1, 1)).thenReturn(post12);
        when(postDao.find(subscription2, 1)).thenReturn(post22);

        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.set(2015, 6, 6, 0, 25, 0);
        final long threshold = cal.getTimeInMillis();
        when(dateHelper.substractDaysFromCurrentDate(30)).thenReturn(threshold);

        cal.set(2015, 6, 6, 0, 0, 0);
        when(post11.getPublishDate()).thenReturn(cal.getTimeInMillis());
        cal.set(2015, 6, 6, 0, 10, 0);
        final long publishDate12 = cal.getTimeInMillis();
        when(post12.getPublishDate()).thenReturn(publishDate12);
        cal.set(2015, 6, 6, 0, 20, 0);
        when(post21.getPublishDate()).thenReturn(cal.getTimeInMillis());
        cal.set(2015, 6, 6, 0, 30, 0);
        when(post22.getPublishDate()).thenReturn(cal.getTimeInMillis());

        when(postDao.listNotBookmarkedAndOlderThan(eq(subscription1), anyLong())).thenReturn(Arrays.asList(post12));
        when(postDao.listNotBookmarkedAndOlderThan(eq(subscription2), anyLong())).thenReturn(Arrays.asList(post22));

        when(subscription1.getGroup()).thenReturn(group);
        when(subscription2.getGroup()).thenReturn(group);

        cal.set(2015, 6, 16, 0, 25, 0);
        when(dateHelper.substractDaysFromCurrentDate(20)).thenReturn(cal.getTimeInMillis());
        when(feedStatDao.listBefore(feed1, cal.getTimeInMillis())).thenReturn(feedStats);

        sut.cleanup(feed1.getUrl(), 30, 1, 20);

        verify(feedDao).find(feed1.getUrl());
        verifyNoMoreInteractions(feedDao);

        verify(subscriptionDao).listSubscriptions(feed1);
        verifyNoMoreInteractions(subscriptionDao);

        verify(postDao).find(subscription1, 1);
        verify(postDao).find(subscription2, 1);

        verify(postDao).listNotBookmarkedAndOlderThan(eq(subscription1), eq(publishDate12));
        verify(postDao).delete(post12);

        verify(postDao).listNotBookmarkedAndOlderThan(eq(subscription2), eq(threshold));
        verify(postDao).delete(post22);
        verifyNoMoreInteractions(postDao);

        verify(feedStatDao).deleteAll(feedStats);
    }

    @Test
    public void cleanup_ShouldDeleteFeedAndStats_IfThereAreNoSubscriptionToFeed() {
        when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Collections.<Subscription> emptyList());
        when(feedStatDao.list(feed1)).thenReturn(feedStats);

        sut.cleanup(feed1.getUrl(), 1, 1, 1);

        verify(feedDao).find(feed1.getUrl());
        verify(subscriptionDao).listSubscriptions(feed1);
        verify(feedStatDao).deleteAll(feedStats);
        verify(feedDao).delete(feed1);
    }

}
