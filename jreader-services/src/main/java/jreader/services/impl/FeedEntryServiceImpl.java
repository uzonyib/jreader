package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jreader.dao.FeedEntryDao;
import jreader.dao.FeedEntryFilter;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.FeedEntryDto;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryService;

import org.springframework.core.convert.ConversionService;

public class FeedEntryServiceImpl extends AbstractService implements FeedEntryService {

    private FeedEntryDao feedEntryDao;

    private ConversionService conversionService;

    public FeedEntryServiceImpl(UserDao userDao, SubscriptionGroupDao subscriptionGroupDao, SubscriptionDao subscriptionDao, FeedEntryDao feedEntryDao,
            ConversionService conversionService) {
        super(userDao, subscriptionGroupDao, subscriptionDao);
        this.feedEntryDao = feedEntryDao;
        this.conversionService = conversionService;
    }

    @Override
    public void markRead(String username, Map<Long, Map<Long, List<Long>>> ids) {
        if (ids == null) {
            return;
        }

        User user = this.getUser(username);

        List<FeedEntry> entriesToSave = new ArrayList<FeedEntry>();
        for (Entry<Long, Map<Long, List<Long>>> groupEntry : ids.entrySet()) {
            SubscriptionGroup group = this.getGroup(user, groupEntry.getKey());
            for (Entry<Long, List<Long>> subscriptionEntry : groupEntry.getValue().entrySet()) {
                Subscription subscription = this.getSubscription(group, subscriptionEntry.getKey());
                for (Long entryId : subscriptionEntry.getValue()) {
                    FeedEntry entry = feedEntryDao.find(subscription, entryId);
                    if (entry != null && !entry.isRead()) {
                        entry.setRead(true);
                        entriesToSave.add(entry);
                    }
                }
            }
        }
        feedEntryDao.saveAll(entriesToSave);
    }

    @Override
    public void star(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId) {
        User user = this.getUser(username);
        SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        Subscription subscription = this.getSubscription(group, subscriptionId);

        FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
        if (feedEntry != null && !feedEntry.isStarred()) {
            feedEntry.setStarred(true);
            feedEntryDao.save(feedEntry);
        }
    }

    @Override
    public void unstar(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId) {
        User user = this.getUser(username);
        SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        Subscription subscription = this.getSubscription(group, subscriptionId);

        FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
        if (feedEntry != null && feedEntry.isStarred()) {
            feedEntry.setStarred(false);
            feedEntryDao.save(feedEntry);
        }
    }

    @Override
    public List<FeedEntryDto> listEntries(FeedEntryFilterData filterData) {
        switch (filterData.getGroup()) {
        case SUBSCRIPTION_GROUP:
            return listSubscriptionGroupEntries(filterData.getUsername(), filterData.getSubscriptionGroupId(), filterData);
        case SUBSCRIPTION:
            return listSubscriptionEntries(filterData.getUsername(), filterData.getSubscriptionGroupId(), filterData.getSubscriptionId(), filterData);
        default:
            return listAllEntries(filterData.getUsername(), filterData);
        }
    }

    private List<FeedEntryDto> listAllEntries(String username, FeedEntryFilter filter) {
        User user = this.getUser(username);
        return convert(feedEntryDao.list(user, filter));
    }

    private List<FeedEntryDto> listSubscriptionGroupEntries(String username, Long subscriptionGroupId, FeedEntryFilter filter) {
        User user = this.getUser(username);
        SubscriptionGroup subscriptionGroup = getGroup(user, subscriptionGroupId);
        return convert(feedEntryDao.list(subscriptionGroup, filter));
    }

    private List<FeedEntryDto> listSubscriptionEntries(String username, Long subscriptionGroupId, Long subscriptionId, FeedEntryFilter filter) {
        User user = this.getUser(username);
        SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        Subscription subscription = this.getSubscription(group, subscriptionId);
        return convert(feedEntryDao.list(subscription, filter));
    }

    private List<FeedEntryDto> convert(List<FeedEntry> feedEntries) {
        List<FeedEntryDto> dtos = new ArrayList<FeedEntryDto>();
        for (FeedEntry feedEntry : feedEntries) {
            dtos.add(conversionService.convert(feedEntry, FeedEntryDto.class));
        }
        return dtos;
    }

}
