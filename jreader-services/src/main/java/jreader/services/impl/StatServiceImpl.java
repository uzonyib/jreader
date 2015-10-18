package jreader.services.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public List<FeedStatsDto> list(String username, int days) {
        User user = this.getUser(username);
        
        long dateAfter = dateHelper.getFirstSecondOfDay(dateHelper.substractDaysFromCurrentDate(days));
        List<FeedStatsDto> stats = new ArrayList<FeedStatsDto>();
        
        for (SubscriptionGroup group : subscriptionGroupDao.list(user)) {
            for (Subscription subscription : subscriptionDao.list(group)) {
                List<FeedStat> feedStats = feedStatDao.list(subscription.getFeed(), dateAfter);
                stats.add(convert(subscription.getFeed(), feedStats));
            }
        }
        
        return stats;
    }

    private FeedStatsDto convert(Feed feed, List<FeedStat> feedStats) {
        FeedStatsDto statsDto = new FeedStatsDto(conversionService.convert(feed, FeedDto.class), new ArrayList<FeedStatDto>());
        
        Map<Long, FeedStatDto> map = new LinkedHashMap<Long, FeedStatDto>();
        for (FeedStat feedStat : feedStats) {
            Long date = dateHelper.getFirstSecondOfDay(feedStat.getRefreshDate());
            FeedStatDto statDto = map.get(date);
            if (statDto == null) {
                statDto = new FeedStatDto(date);
                map.put(date, statDto);
            }
            statDto.addCount(feedStat.getCount());
        }
        
        for (FeedStatDto statDto : map.values()) {
            statsDto.getStats().add(statDto);
        }
        
        return statsDto;
    }

}
