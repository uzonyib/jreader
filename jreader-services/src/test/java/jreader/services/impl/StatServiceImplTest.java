package jreader.services.impl;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.FeedStatDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;
import jreader.dto.FeedDto;
import jreader.dto.FeedStatDto;
import jreader.dto.FeedStatsDto;
import jreader.services.DateHelper;

public class StatServiceImplTest {
    
    private static final String USERNAME = "test_user";

    @InjectMocks
    private StatServiceImpl sut;
    
    @Mock
    private UserDao userDao;
    @Mock
    private GroupDao groupDao;
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
    private Group group;
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
    
    @Mock
    private FeedStatDto statDto21;
    @Mock
    private FeedStatDto statDto22;
    
    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void list() {
        when(userDao.find(USERNAME)).thenReturn(user);
        when(groupDao.list(user)).thenReturn(Arrays.asList(group));
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription1, subscription2));
        
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Calendar daysBefore = Calendar.getInstance(timeZone);
        daysBefore.set(2015, 9, 10, 18, 11, 21);
        when(dateHelper.substractDaysFromCurrentDate(5)).thenReturn(daysBefore.getTimeInMillis());
        Calendar minDate = Calendar.getInstance(timeZone);
        minDate.set(2015, 9, 10, 18, 11, 21);
        when(dateHelper.getFirstSecondOfDay(daysBefore.getTimeInMillis())).thenReturn(minDate.getTimeInMillis());
        
        when(subscription1.getFeed()).thenReturn(feed1);
        when(subscription2.getFeed()).thenReturn(feed2);
        
        when(conversionService.convert(feed1, FeedDto.class)).thenReturn(feedDto1);
        when(conversionService.convert(feed2, FeedDto.class)).thenReturn(feedDto2);
        
        when(feedStatDao.listAfter(feed1, minDate.getTimeInMillis())).thenReturn(new ArrayList<FeedStat>());
        when(feedStatDao.listAfter(feed2, minDate.getTimeInMillis())).thenReturn(Arrays.asList(stat21, stat22));
        
        Calendar date21 = Calendar.getInstance(timeZone);
        date21.set(2015, 9, 16, 0, 0, 30);
        when(stat21.getCount()).thenReturn(1);
        when(stat21.getRefreshDate()).thenReturn(date21.getTimeInMillis());
        
        Calendar date22 = Calendar.getInstance(timeZone);
        date22.set(2015, 9, 14, 0, 0, 0);
        when(stat22.getCount()).thenReturn(2);
        when(stat22.getRefreshDate()).thenReturn(date22.getTimeInMillis());
        
        when(conversionService.convert(Collections.emptyList(),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStat.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStatDto.class)))).thenReturn(Collections.emptyList());
        
        when(conversionService.convert(Arrays.asList(stat21, stat22),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStat.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStatDto.class)))).thenReturn(Arrays.asList(statDto21, statDto22));
        
        List<FeedStatsDto> stats = sut.list(USERNAME, 5);
        
        assertNotNull(stats);
        assertEquals(stats.size(), 2);
        
        assertEquals(stats.get(0).getFeed(), feedDto1);
        assertEquals(stats.get(0).getStats().size(), 0);
        
        assertEquals(stats.get(1).getFeed(), feedDto2);
        assertEquals(stats.get(1).getStats().size(), 2);
        assertSame(stats.get(1).getStats().get(0), statDto21);
        assertSame(stats.get(1).getStats().get(1), statDto22);
    }

}
