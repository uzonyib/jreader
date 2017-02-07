package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import jreader.dao.FeedStatDao;
import jreader.dao.GroupDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;
import jreader.domain.Group;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.FeedDto;
import jreader.dto.FeedStatDto;
import jreader.dto.FeedStatsDto;
import jreader.services.DateHelper;

public class StatServiceImplTest extends ServiceTest {

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
    private Feed feed1;
    @Mock
    private Feed feed2;
    @Mock
    private FeedStat stat21;
    @Mock
    private FeedStat stat22;

    private FeedDto feedDto1;
    private FeedDto feedDto2;

    private FeedStatDto statDto21 = new FeedStatDto(1000L, 2);
    private FeedStatDto statDto22 = new FeedStatDto(1100L, 3);

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        feedDto1 = FeedDto.builder().title("feed1").build();
        feedDto2 = FeedDto.builder().title("feed2").build();

        final DaoFacade daoFacade = DaoFacade.builder().userDao(userDao).groupDao(groupDao).subscriptionDao(subscriptionDao).feedStatDao(feedStatDao).build();
        sut = new StatServiceImpl(daoFacade, conversionService, dateHelper);
    }

    @Test
    public void list_ShouldReturnStats() {
        final String username = "user";
        when(userDao.find(username)).thenReturn(user);
        when(groupDao.list(user)).thenReturn(Arrays.asList(group));
        when(subscriptionDao.list(group)).thenReturn(Arrays.asList(subscription1, subscription2));

        final TimeZone timeZone = TimeZone.getTimeZone("GMT");
        final Calendar daysBefore = Calendar.getInstance(timeZone);
        daysBefore.set(2015, 9, 10, 18, 11, 21);
        when(dateHelper.substractDaysFromCurrentDate(5)).thenReturn(daysBefore.getTimeInMillis());
        final Calendar minDate = Calendar.getInstance(timeZone);
        minDate.set(2015, 9, 10, 18, 11, 21);
        when(dateHelper.getFirstSecondOfDay(daysBefore.getTimeInMillis())).thenReturn(minDate.getTimeInMillis());

        when(subscription1.getFeed()).thenReturn(feed1);
        when(subscription2.getFeed()).thenReturn(feed2);

        when(conversionService.convert(feed1, FeedDto.class)).thenReturn(feedDto1);
        when(conversionService.convert(feed2, FeedDto.class)).thenReturn(feedDto2);

        when(feedStatDao.listAfter(feed1, minDate.getTimeInMillis())).thenReturn(new ArrayList<FeedStat>());
        when(feedStatDao.listAfter(feed2, minDate.getTimeInMillis())).thenReturn(Arrays.asList(stat21, stat22));

        when(conversionService.convert(Collections.emptyList(),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStat.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStatDto.class)))).thenReturn(Collections.emptyList());

        final List<FeedStatDto> statDtos2 = Arrays.asList(statDto21, statDto22);
        when(conversionService.convert(Arrays.asList(stat21, stat22),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStat.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStatDto.class)))).thenReturn(statDtos2);

        final List<FeedStatsDto> actual = sut.list(username, 5);

        assertThat(actual).hasSize(2);

        assertThat(actual.get(0).getFeed()).isEqualTo(feedDto1);
        assertThat(actual.get(0).getStats()).isEmpty();

        assertThat(actual.get(1).getFeed()).isEqualTo(feedDto2);
        assertThat(actual.get(1).getStats()).isEqualTo(statDtos2);
    }

}
