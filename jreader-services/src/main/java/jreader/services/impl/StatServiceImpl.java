package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;

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
import jreader.dto.FeedStatDto;
import jreader.dto.FeedStatsDto;
import jreader.services.DateHelper;
import jreader.services.StatService;

public class StatServiceImpl extends AbstractService implements StatService {

    private FeedStatDao feedStatDao;
    
    private ConversionService conversionService;
    
    private DateHelper dateHelper;

    public StatServiceImpl(final UserDao userDao, final SubscriptionGroupDao subscriptionGroupDao, final SubscriptionDao subscriptionDao,
            final FeedStatDao feedStatDao, final ConversionService conversionService, final DateHelper dateHelper) {
        super(userDao, subscriptionGroupDao, subscriptionDao);
        this.feedStatDao = feedStatDao;
        this.conversionService = conversionService;
        this.dateHelper = dateHelper;
    }

    @Override
    public List<FeedStatsDto> list(final String username, final int days) {
        final User user = this.getUser(username);
        
        final long dateAfter = dateHelper.getFirstSecondOfDay(dateHelper.substractDaysFromCurrentDate(days));
        final List<FeedStatsDto> stats = new ArrayList<FeedStatsDto>();
        
        for (final SubscriptionGroup group : subscriptionGroupDao.list(user)) {
            for (final Subscription subscription : subscriptionDao.list(group)) {
                final List<FeedStat> feedStats = feedStatDao.listAfter(subscription.getFeed(), dateAfter);
                stats.add(convert(subscription.getFeed(), feedStats));
            }
        }
        
        return stats;
    }

    private FeedStatsDto convert(final Feed feed, final List<FeedStat> feedStats) {
        final FeedStatsDto statsDto = new FeedStatsDto(conversionService.convert(feed, FeedDto.class), new ArrayList<FeedStatDto>());
        for (final FeedStat feedStat : feedStats) {
            statsDto.getStats().add(conversionService.convert(feedStat, FeedStatDto.class));
        }
        return statsDto;
    }

}
