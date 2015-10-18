package jreader.services.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedStatDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.FeedDto;
import jreader.dto.FeedStatsDto;
import jreader.services.DateHelper;

public class StatServiceImplTest {
    
    private static final String USERNAME = "test_user";

    @InjectMocks
    private StatServiceImpl sut;
    
    @Mock
    private UserDao userDao;
    @Mock
    private SubscriptionGroupDao subscriptionGroupDao;
    @Mock
    private SubscriptionDao subscriptionDao;
    @Mock
    private FeedStatDao feedStatDao;
    @Mock
    private ConversionService conversionService;
    @Mock
    private DateHelper dateHelper;
    
    @Mock
    private User user;
    @Mock
    private SubscriptionGroup group;
    @Mock
    private Subscription subscription1;
    @Mock
    private Subscription subscription2;
    @Mock
    private Subscription subscription3;
    @Mock
    private Feed feed1;
    @Mock
    private Feed feed2;
    @Mock
    private Feed feed3;
    @Mock
    private FeedStat stat21;
    @Mock
    private FeedStat stat22;
    @Mock
    private FeedStat stat31;
    @Mock
    private FeedStat stat32;
    @Mock
    private FeedStat stat33;
    
    @Mock
    private FeedDto feedDto1;
    @Mock
    private FeedDto feedDto2;
    @Mock
    private FeedDto feedDto3;
    
    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void list() {
        when(userDao.find(USERNAME)).thenReturn(user);
        when(subscriptionGroupDao.list(user)).thenReturn(Arrays.asList(group));
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription1, subscription2, subscription3));
        
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Calendar daysBefore = Calendar.getInstance(timeZone);
        daysBefore.set(2015, 9, 10, 18, 11, 21);
        when(dateHelper.substractDaysFromCurrentDate(5)).thenReturn(daysBefore.getTimeInMillis());
        Calendar minDate = Calendar.getInstance(timeZone);
        minDate.set(2015, 9, 10, 18, 11, 21);
        when(dateHelper.getFirstSecondOfDay(daysBefore.getTimeInMillis())).thenReturn(minDate.getTimeInMillis());
        
        when(subscription1.getFeed()).thenReturn(feed1);
        when(subscription2.getFeed()).thenReturn(feed2);
        when(subscription3.getFeed()).thenReturn(feed3);
        
        when(conversionService.convert(feed1, FeedDto.class)).thenReturn(feedDto1);
        when(conversionService.convert(feed2, FeedDto.class)).thenReturn(feedDto2);
        when(conversionService.convert(feed3, FeedDto.class)).thenReturn(feedDto3);
        
        when(feedStatDao.list(feed1, minDate.getTimeInMillis())).thenReturn(new ArrayList<FeedStat>());
        when(feedStatDao.list(feed2, minDate.getTimeInMillis())).thenReturn(Arrays.asList(stat21, stat22));
        when(feedStatDao.list(feed3, minDate.getTimeInMillis())).thenReturn(Arrays.asList(stat31, stat32, stat33));
        
        Calendar date21 = Calendar.getInstance(timeZone);
        date21.set(2015, 9, 16, 10, 20, 30);
        Calendar mn21 = Calendar.getInstance(timeZone);
        mn21.set(2015, 9, 16, 0, 0, 0);
        when(dateHelper.getFirstSecondOfDay(date21.getTimeInMillis())).thenReturn(mn21.getTimeInMillis());
        when(stat21.getCount()).thenReturn(1);
        when(stat21.getRefreshDate()).thenReturn(date21.getTimeInMillis());
        
        Calendar date22 = Calendar.getInstance(timeZone);
        date22.set(2015, 9, 15, 15, 33, 45);
        Calendar mn22 = Calendar.getInstance(timeZone);
        mn22.set(2015, 9, 15, 0, 0, 0);
        when(dateHelper.getFirstSecondOfDay(date22.getTimeInMillis())).thenReturn(mn22.getTimeInMillis());
        when(stat22.getCount()).thenReturn(2);
        when(stat22.getRefreshDate()).thenReturn(date22.getTimeInMillis());
        
        Calendar date31 = Calendar.getInstance(timeZone);
        date31.set(2015, 9, 15, 9, 0, 12);
        Calendar mn31 = Calendar.getInstance(timeZone);
        mn31.set(2015, 9, 15, 0, 0, 0);
        when(dateHelper.getFirstSecondOfDay(date31.getTimeInMillis())).thenReturn(mn31.getTimeInMillis());
        when(stat31.getCount()).thenReturn(3);
        when(stat31.getRefreshDate()).thenReturn(date31.getTimeInMillis());
        
        Calendar date32 = Calendar.getInstance(timeZone);
        date32.set(2015, 9, 15, 22, 51, 57);
        Calendar mn32 = Calendar.getInstance(timeZone);
        mn32.set(2015, 9, 15, 0, 0, 0);
        when(dateHelper.getFirstSecondOfDay(date32.getTimeInMillis())).thenReturn(mn32.getTimeInMillis());
        when(stat32.getCount()).thenReturn(4);
        when(stat32.getRefreshDate()).thenReturn(date32.getTimeInMillis());
        
        Calendar date33 = Calendar.getInstance(timeZone);
        date33.set(2015, 9, 16, 0, 5, 42);
        Calendar mn33 = Calendar.getInstance(timeZone);
        mn33.set(2015, 9, 16, 0, 0, 0);
        when(dateHelper.getFirstSecondOfDay(date33.getTimeInMillis())).thenReturn(mn33.getTimeInMillis());
        when(stat33.getCount()).thenReturn(5);
        when(stat33.getRefreshDate()).thenReturn(date33.getTimeInMillis());
        
        List<FeedStatsDto> stats = sut.list(USERNAME, 5);
        
        assertNotNull(stats);
        assertEquals(stats.size(), 3);
        
        assertEquals(stats.get(0).getFeed(), feedDto1);
        assertEquals(stats.get(0).getStats().size(), 0);
        
        assertEquals(stats.get(1).getFeed(), feedDto2);
        assertEquals(stats.get(1).getStats().size(), 2);
        assertEquals(stats.get(1).getStats().get(0).getCount(), 1);
        assertEquals(stats.get(1).getStats().get(0).getDate(), mn21.getTimeInMillis());
        assertEquals(stats.get(1).getStats().get(1).getCount(), 2);
        assertEquals(stats.get(1).getStats().get(1).getDate(), mn22.getTimeInMillis());
        
        assertEquals(stats.get(2).getFeed(), feedDto3);
        assertEquals(stats.get(2).getStats().size(), 2);
        assertEquals(stats.get(2).getStats().get(0).getCount(), 7);
        assertEquals(stats.get(2).getStats().get(0).getDate(), mn31.getTimeInMillis());
        assertEquals(stats.get(2).getStats().get(1).getCount(), 5);
        assertEquals(stats.get(2).getStats().get(1).getDate(), mn33.getTimeInMillis());
    }

}
