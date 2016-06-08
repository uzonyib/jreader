package jreader.services.impl;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.DaoFacade;
import jreader.dao.FeedDao;
import jreader.dao.FeedStatDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Feed;
import jreader.domain.FeedStat;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.FeedDto;
import jreader.dto.RssFetchResult;
import jreader.services.DateHelper;
import jreader.services.RssService;

public class CronServiceImplTest {
	
    private static final String USERNAME = "username";
	private static final String FEED_URL = "url";
	private static final String FEED_TITLE = "title";

	private CronServiceImpl service;
	
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
	private BuilderFactory builderFactory;
	
	@Mock
	private DateHelper dateHelper;
	
	@Mock
	private Feed feed1;
	@Mock
	private Feed feed2;
	
	private RssFetchResult fetchResult;
	
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
	private FeedStat.Builder builder;
	
	@Mock
	private FeedStat feedStat1;
	@Mock
    private FeedStat feedStat2;
	@Mock
    private List<FeedStat> feedStats;
	
	@Mock
	private Group group;
	
	@Mock
	private User user;
	
    @Mock
    private FeedDto dto1;
    @Mock
    private FeedDto dto2;
	
	@BeforeMethod
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		fetchResult = new RssFetchResult(null, Arrays.asList(post11, post12, post13));
		
		DaoFacade daoFacade = DaoFacade.builder().subscriptionDao(subscriptionDao).feedDao(feedDao).postDao(postDao).feedStatDao(feedStatDao).build();
		service = new CronServiceImpl(daoFacade, rssService, conversionService, builderFactory, dateHelper);
	}
	
	@Test
    public void listFeeds() {
        List<Feed> entities = Arrays.asList(feed1, feed2);
        when(feedDao.listAll()).thenReturn(entities);
        when(conversionService.convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Feed.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedDto.class)))).thenReturn(Arrays.asList(dto1, dto2));
        
        List<FeedDto> dtos = service.listFeeds();
        
        verify(feedDao).listAll();
        
        verify(conversionService).convert(entities,
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Feed.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedDto.class)));
        
        assertEquals(dtos.size(), 2);
        assertEquals(dtos.get(0), dto1);
        assertEquals(dtos.get(1), dto2);
    }
	
	@Test
	public void refreshFeeds_NewPosts() {
	    when(feedDao.find(FEED_URL)).thenReturn(feed1);
		when(feed1.getUrl()).thenReturn(FEED_URL);
		when(feed1.getTitle()).thenReturn(FEED_TITLE);
		when(feed1.getStatus()).thenReturn(null);
		
		long date = 1445271922000L;
		when(feed1.getLastUpdateDate()).thenReturn(date - 1000 * 60 * 20);
		when(dateHelper.getCurrentDate()).thenReturn(date);
		
		when(rssService.fetch(FEED_URL)).thenReturn(fetchResult);
		
		when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1, subscription2));
		
		when(subscription1.getLastUpdateDate()).thenReturn(date - 1000 * 60 * 20);
		when(subscription2.getLastUpdateDate()).thenReturn(date - 1000 * 60 * 20);
		
		DateHelper dh = new DateHelperImpl();
		long pubDate1 = date - 1000 * 60 * 30;
        when(post11.getPublishDate()).thenReturn(pubDate1);
		when(post11.getUri()).thenReturn("uri1");
		long day = dh.getFirstSecondOfDay(pubDate1);
        when(dateHelper.getFirstSecondOfDay(pubDate1)).thenReturn(day);
		
		long pubDate2 = date - 1000 * 60 * 10;
        when(post12.getPublishDate()).thenReturn(pubDate2);
		when(post12.getUri()).thenReturn("uri2");
		when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(dh.getFirstSecondOfDay(pubDate2));
		
		when(post13.getPublishDate()).thenReturn(pubDate2);
		when(post13.getUri()).thenReturn("uri3");
		
		when(subscription1.getGroup()).thenReturn(group);
		when(subscription2.getGroup()).thenReturn(group);
		when(group.getUser()).thenReturn(user);
		when(user.getUsername()).thenReturn(USERNAME);
		
		when(builderFactory.createFeedStatBuilder()).thenReturn(builder);
		when(builder.feed(feed1)).thenReturn(builder);
		when(builder.refreshDate(anyLong())).thenReturn(builder);
		when(builder.count(anyInt())).thenReturn(builder);
		when(builder.build()).thenReturn(feedStat1);
		
		when(feedStat1.getCount()).thenReturn(1);
		
		service.refresh(FEED_URL);
		
		verify(rssService).fetch(FEED_URL);
		
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
        
        verify(feed1).setLastRefreshDate(date);
        verify(feed1).setStatus(0);
        verify(feedDao).save(feed1);
        
        verify(builder).feed(feed1);
        verify(builder).refreshDate(day);
        verify(builder).count(1);
        verify(builder).build();
        verify(feedStat1).setCount(2);
        verify(feedStatDao).saveAll(Arrays.asList(feedStat1));
	}
	
	@Test
    public void refreshFeeds_NewPostInTheFuture() {
        when(feedDao.find(FEED_URL)).thenReturn(feed1);
        when(feed1.getUrl()).thenReturn(FEED_URL);
        when(feed1.getTitle()).thenReturn(FEED_TITLE);
        when(feed1.getStatus()).thenReturn(2);
        
        long date = 1445271922000L;
        when(feed1.getLastRefreshDate()).thenReturn(date - 1000 * 60 * 20);
        when(dateHelper.getCurrentDate()).thenReturn(date);
        
        when(rssService.fetch(FEED_URL)).thenReturn(new RssFetchResult(null, Arrays.asList(post11)));
        
        when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1));
        
        when(subscription1.getLastUpdateDate()).thenReturn(date - 1000 * 60 * 20);
        
        DateHelper dh = new DateHelperImpl();
        long pubDate1 = date + 1000 * 60 * 30;
        when(post11.getPublishDate()).thenReturn(pubDate1);
        when(post11.getUri()).thenReturn("uri1");
        long day = dh.getFirstSecondOfDay(pubDate1);
        when(dateHelper.getFirstSecondOfDay(pubDate1)).thenReturn(day);
        
        when(subscription1.getGroup()).thenReturn(group);
        when(group.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn(USERNAME);
        
        service.refresh(FEED_URL);
        
        verify(rssService).fetch(FEED_URL);
        
        verify(subscriptionDao).listSubscriptions(feed1);
        
        verifyNoMoreInteractions(postDao);
        
    }
	
	@Test
    public void refreshFeeds_Failure_StatusIsNull() {
        when(feedDao.find(FEED_URL)).thenReturn(feed1);
        when(feed1.getUrl()).thenReturn(FEED_URL);
        when(feed1.getTitle()).thenReturn(FEED_TITLE);
        when(feed1.getStatus()).thenReturn(null);
        
        when(rssService.fetch(FEED_URL)).thenReturn(null);
        
        service.refresh(FEED_URL);
        
        verify(rssService).fetch(FEED_URL);
        
        verify(feed1).setStatus(1);
        verify(feedDao).save(feed1);
    }
	
	@Test
    public void refreshFeeds_Failure_StatusIsIncremented() {
        when(feedDao.find(FEED_URL)).thenReturn(feed1);
        when(feed1.getUrl()).thenReturn(FEED_URL);
        when(feed1.getTitle()).thenReturn(FEED_TITLE);
        when(feed1.getStatus()).thenReturn(3);
        
        when(rssService.fetch(FEED_URL)).thenReturn(null);
        
        service.refresh(FEED_URL);
        
        verify(rssService).fetch(FEED_URL);
        
        verify(feed1).setStatus(4);
        verify(feedDao).save(feed1);
    }
	
	@Test
    public void refreshFeeds_Failure_StatusIsMax() {
        when(feedDao.find(FEED_URL)).thenReturn(feed1);
        when(feed1.getUrl()).thenReturn(FEED_URL);
        when(feed1.getTitle()).thenReturn(FEED_TITLE);
        when(feed1.getStatus()).thenReturn(5);
        
        when(rssService.fetch(FEED_URL)).thenReturn(null);
        
        service.refresh(FEED_URL);
        
        verify(rssService).fetch(FEED_URL);
        
        verify(feed1).setStatus(5);
        verify(feedDao).save(feed1);
    }
	
	@Test
	public void isNew_Feed_PublishDateIsNull() {
	    when(post11.getPublishDate()).thenReturn(null);
	    
	    boolean isNew = service.isNew(post11, feed1, 1100L);
	    
	    assertFalse(isNew);
	}
	
	@Test
    public void isNew_Feed_UriIsNull() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn(null);
        
        boolean isNew = service.isNew(post11, feed1, 1100L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Feed_PublishedDateIsInFuture() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        
        boolean isNew = service.isNew(post11, feed1, 999L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Feed_FeedLastUpdateDateIsLarger() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(feed1.getLastUpdateDate()).thenReturn(1100L);
        
        boolean isNew = service.isNew(post11, feed1, 1000L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Feed_FeedLastUpdateDateIsNull() {
	    when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(feed1.getLastUpdateDate()).thenReturn(null);
        
        boolean isNew = service.isNew(post11, feed1, 1100L);
        
        assertTrue(isNew);
    }
	
	@Test
    public void isNew_Feed_PublishDateIsEqualToLastUpdateDate() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(feed1.getLastUpdateDate()).thenReturn(1000L);
        
        boolean isNew = service.isNew(post11, feed1, 1100L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Feed_PublishDateIsLargerThanUpdatedDate() {
        when(post11.getPublishDate()).thenReturn(1001L);
        when(post11.getUri()).thenReturn("uri");
        when(feed1.getLastUpdateDate()).thenReturn(1000L);
        
        boolean isNew = service.isNew(post11, feed1, 1100L);
        
        assertTrue(isNew);
    }
	
	@Test
    public void isNew_Subscription_PublishDateIsNull() {
        when(post11.getPublishDate()).thenReturn(null);
        
        boolean isNew = service.isNew(post11, subscription1, 1100L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_UriIsNull() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn(null);
        
        boolean isNew = service.isNew(post11, subscription1, 1100L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_PublishDateIsInFuture() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        
        boolean isNew = service.isNew(post11, subscription1, 999L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_PublishDateIsSmaller() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(subscription1.getLastUpdateDate()).thenReturn(1100L);
        
        boolean isNew = service.isNew(post11, subscription1, 1000L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_PublishDateIsEqual() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(subscription1.getLastUpdateDate()).thenReturn(1000L);
        when(postDao.find(subscription1, "uri", 1000L)).thenReturn(null);
        
        boolean isNew = service.isNew(post11, subscription1, 1100L);
        
        assertTrue(isNew);
    }
	
	@Test
    public void isNew_Subscription_PostFound() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(subscription1.getLastUpdateDate()).thenReturn(900L);
        when(postDao.find(subscription1, "uri", 1000L)).thenReturn(post11);
        
        boolean isNew = service.isNew(post11, subscription1, 1100L);
        
        assertFalse(isNew);
    }
	
	@Test
    public void isNew_Subscription_PostNotFound() {
        when(post11.getPublishDate()).thenReturn(1000L);
        when(post11.getUri()).thenReturn("uri");
        when(subscription1.getLastUpdateDate()).thenReturn(900L);
        when(postDao.find(subscription1, "uri", 1000L)).thenReturn(null);
        
        boolean isNew = service.isNew(post11, subscription1, 1100L);
        
        assertTrue(isNew);
    }
	
	@Test
	public void updateFeed_SameDay() {
	    when(feed1.getLastUpdateDate()).thenReturn(850L);
	    
	    long currentDate = 1000L;
	    
	    long pubDate1 = 700L;
        when(post11.getPublishDate()).thenReturn(pubDate1);
        when(post11.getUri()).thenReturn("uri1");
        
	    long pubDate2 = 900L;
        when(post12.getPublishDate()).thenReturn(pubDate2);
        when(post12.getUri()).thenReturn("uri2");
        long day = 800L;
        when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(day);
        
        long pubDate3 = 900L;
        when(post13.getPublishDate()).thenReturn(pubDate3);
        when(post13.getUri()).thenReturn("uri3");
        when(dateHelper.getFirstSecondOfDay(pubDate3)).thenReturn(day);
	    
        when(builderFactory.createFeedStatBuilder()).thenReturn(builder);
        when(builder.feed(feed1)).thenReturn(builder);
        when(builder.refreshDate(anyLong())).thenReturn(builder);
        when(builder.count(anyInt())).thenReturn(builder);
        when(builder.build()).thenReturn(feedStat1);
        when(feedStat1.getCount()).thenReturn(1);
        
	    service.updateFeed(feed1, currentDate, fetchResult);
	    
	    verify(feed1).setLastRefreshDate(currentDate);
	    verify(feedDao).save(feed1);
	    
	    verify(builder).feed(feed1);
        verify(builder).refreshDate(day);
        verify(builder).count(1);
        verify(builder).build();
        verify(feedStat1).setCount(2);
        verify(feedStatDao).saveAll(Arrays.asList(feedStat1));
	}
	
	@Test
    public void updateFeed_DifferentDay() {
        when(feed1.getLastUpdateDate()).thenReturn(850L);
        
        long currentDate = 1000L;
        
        long pubDate1 = 700L;
        when(post11.getPublishDate()).thenReturn(pubDate1);
        when(post11.getUri()).thenReturn("uri1");
        
        long pubDate2 = 900L;
        when(post12.getPublishDate()).thenReturn(pubDate2);
        when(post12.getUri()).thenReturn("uri2");
        long day2 = 800L;
        when(dateHelper.getFirstSecondOfDay(pubDate2)).thenReturn(day2);
        
        long pubDate3 = 950L;
        when(post13.getPublishDate()).thenReturn(pubDate3);
        when(post13.getUri()).thenReturn("uri3");
        long day3 = 900L;
        when(dateHelper.getFirstSecondOfDay(pubDate3)).thenReturn(day3);
        
        when(feedStatDao.find(feed1, day2)).thenReturn(feedStat1);
        
        when(feedStat1.getCount()).thenReturn(5);
        
        when(builderFactory.createFeedStatBuilder()).thenReturn(builder);
        when(builder.feed(feed1)).thenReturn(builder);
        when(builder.refreshDate(anyLong())).thenReturn(builder);
        when(builder.count(anyInt())).thenReturn(builder);
        when(builder.build()).thenReturn(feedStat2);
        when(feedStat2.getCount()).thenReturn(1);
        
        service.updateFeed(feed1, currentDate, fetchResult);
        
        verify(feed1).setLastRefreshDate(currentDate);
        verify(feedDao).save(feed1);
        
        verify(feedStat1).setCount(6);
        
        verify(builder).feed(feed1);
        verify(builder).refreshDate(day3);
        verify(builder).count(1);
        verify(builder).build();
        
        verify(feedStatDao).saveAll(Arrays.asList(feedStat1, feedStat2));
    }
	
	@Test
	public void cleanUp() {
		when(feedDao.find(FEED_URL)).thenReturn(feed1);
		
		when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Arrays.asList(subscription1, subscription2));
		
		when(postDao.find(subscription1, 1)).thenReturn(post12);
		when(postDao.find(subscription2, 1)).thenReturn(post22);
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		cal.set(2015, 6, 6, 0, 25, 0);
		long threshold = cal.getTimeInMillis();
		when(dateHelper.substractDaysFromCurrentDate(30)).thenReturn(threshold);
		
        cal.set(2015, 6, 6, 0, 0, 0);
		when(post11.getPublishDate()).thenReturn(cal.getTimeInMillis());
		cal.set(2015, 6, 6, 0, 10, 0);
		long publishDate12 = cal.getTimeInMillis();
		when(post12.getPublishDate()).thenReturn(publishDate12);
		cal.set(2015, 6, 6, 0, 20, 0);
		when(post21.getPublishDate()).thenReturn(cal.getTimeInMillis());
		cal.set(2015, 6, 6, 0, 30, 0);
		when(post22.getPublishDate()).thenReturn(cal.getTimeInMillis());
		
		when(postDao.listNotBookmarkedAndOlderThan(eq(subscription1), anyLong())).thenReturn(Arrays.asList(post12));
		when(postDao.listNotBookmarkedAndOlderThan(eq(subscription2), anyLong())).thenReturn(Arrays.asList(post22));
		
		when(subscription1.getGroup()).thenReturn(group);
		when(subscription2.getGroup()).thenReturn(group);
		when(group.getUser()).thenReturn(user);
		
		cal.set(2015, 6, 16, 0, 25, 0);
		when(dateHelper.substractDaysFromCurrentDate(20)).thenReturn(cal.getTimeInMillis());
		when(feedStatDao.listBefore(feed1, cal.getTimeInMillis())).thenReturn(feedStats);
		
		service.cleanup(FEED_URL, 30, 1, 20);
		
		verify(feedDao).find(FEED_URL);
		
		verify(subscriptionDao).listSubscriptions(feed1);
		
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
    public void cleanUpFeedWithoutSubscription() {
        when(feedDao.find(FEED_URL)).thenReturn(feed1);
        when(subscriptionDao.listSubscriptions(feed1)).thenReturn(Collections.<Subscription>emptyList());
        when(feedStatDao.list(feed1)).thenReturn(feedStats);
        
        service.cleanup(FEED_URL, 1, 1, 1);
        
        verify(feedDao).find(FEED_URL);
        verify(subscriptionDao).listSubscriptions(feed1);
        verify(feedStatDao).deleteAll(feedStats);
        verify(feedDao).delete(feed1);
    }

}
