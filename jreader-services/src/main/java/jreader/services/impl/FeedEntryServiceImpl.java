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

    public FeedEntryServiceImpl(final UserDao userDao, final SubscriptionGroupDao subscriptionGroupDao, final SubscriptionDao subscriptionDao,
            final FeedEntryDao feedEntryDao, final ConversionService conversionService) {
        super(userDao, subscriptionGroupDao, subscriptionDao);
        this.feedEntryDao = feedEntryDao;
        this.conversionService = conversionService;
    }

    @Override
    public void markRead(final String username, final Map<Long, Map<Long, List<Long>>> ids) {
        if (ids == null) {
            return;
        }

        final User user = this.getUser(username);

        final List<FeedEntry> entriesToSave = new ArrayList<FeedEntry>();
        for (final Entry<Long, Map<Long, List<Long>>> groupEntry : ids.entrySet()) {
            final SubscriptionGroup group = this.getGroup(user, groupEntry.getKey());
            for (final Entry<Long, List<Long>> subscriptionEntry : groupEntry.getValue().entrySet()) {
                final Subscription subscription = this.getSubscription(group, subscriptionEntry.getKey());
                for (final Long entryId : subscriptionEntry.getValue()) {
                    final FeedEntry entry = feedEntryDao.find(subscription, entryId);
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
    public void star(final String username, final Long subscriptionGroupId, final Long subscriptionId, final Long feedEntryId) {
        final User user = this.getUser(username);
        final SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
        if (feedEntry != null && !feedEntry.isStarred()) {
            feedEntry.setStarred(true);
            feedEntryDao.save(feedEntry);
        }
    }

    @Override
    public void unstar(final String username, final Long subscriptionGroupId, final Long subscriptionId, final Long feedEntryId) {
        final User user = this.getUser(username);
        final SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
        if (feedEntry != null && feedEntry.isStarred()) {
            feedEntry.setStarred(false);
            feedEntryDao.save(feedEntry);
        }
    }

    @Override
    public List<FeedEntryDto> listEntries(final FeedEntryFilterData filterData) {
        switch (filterData.getGroup()) {
        case SUBSCRIPTION_GROUP:
            return listSubscriptionGroupEntries(filterData.getUsername(), filterData.getSubscriptionGroupId(), filterData);
        case SUBSCRIPTION:
            return listSubscriptionEntries(filterData.getUsername(), filterData.getSubscriptionGroupId(), filterData.getSubscriptionId(), filterData);
        default:
            return listAllEntries(filterData.getUsername(), filterData);
        }
    }

    private List<FeedEntryDto> listAllEntries(final String username, final FeedEntryFilter filter) {
        User user = this.getUser(username);
        return convert(feedEntryDao.list(user, filter));
    }

    private List<FeedEntryDto> listSubscriptionGroupEntries(final String username, final Long subscriptionGroupId, final FeedEntryFilter filter) {
        User user = this.getUser(username);
        SubscriptionGroup subscriptionGroup = getGroup(user, subscriptionGroupId);
        return convert(feedEntryDao.list(subscriptionGroup, filter));
    }

    private List<FeedEntryDto> listSubscriptionEntries(final String username, final Long subscriptionGroupId, final Long subscriptionId,
            final FeedEntryFilter filter) {
        final User user = this.getUser(username);
        final SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);
        return convert(feedEntryDao.list(subscription, filter));
    }

    private List<FeedEntryDto> convert(final List<FeedEntry> feedEntries) {
        final List<FeedEntryDto> dtos = new ArrayList<FeedEntryDto>();
        for (final FeedEntry feedEntry : feedEntries) {
            dtos.add(conversionService.convert(feedEntry, FeedEntryDto.class));
        }
        return dtos;
    }

}
