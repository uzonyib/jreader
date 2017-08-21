package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import jreader.dao.DaoFacade;
import jreader.dao.FeedStatDao;
import jreader.domain.FeedStat;
import jreader.domain.Group;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.FeedDto;
import jreader.dto.FeedStatDto;
import jreader.dto.FeedStatsDto;
import jreader.services.DateHelper;
import jreader.services.StatService;

@Service
public class StatServiceImpl extends AbstractService implements StatService {

    private FeedStatDao feedStatDao;
    
    private ConversionService conversionService;
    
    private DateHelper dateHelper;

    @Autowired
    public StatServiceImpl(final DaoFacade daoFacade, final ConversionService conversionService, final DateHelper dateHelper) {
        super(daoFacade.getUserDao(), daoFacade.getGroupDao(), daoFacade.getSubscriptionDao());
        this.feedStatDao = daoFacade.getFeedStatDao();
        this.conversionService = conversionService;
        this.dateHelper = dateHelper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FeedStatsDto> list(final String username, final int days) {
        final User user = this.getUser(username);
        
        final long dateAfter = dateHelper.getFirstSecondOfDay(dateHelper.substractDaysFromCurrentDate(days));
        final List<FeedStatsDto> stats = new ArrayList<FeedStatsDto>();
        
        for (final Group group : groupDao.list(user)) {
            for (final Subscription subscription : subscriptionDao.list(group)) {
                final List<FeedStat> feedStats = feedStatDao.listAfter(subscription.getFeed(), dateAfter);
                stats.add(new FeedStatsDto(conversionService.convert(subscription.getFeed(), FeedDto.class),
                        (List<FeedStatDto>) conversionService.convert(feedStats,
                                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStat.class)),
                                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedStatDto.class)))));
            }
        }
        
        return stats;
    }

}
