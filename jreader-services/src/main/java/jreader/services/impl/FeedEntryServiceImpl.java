package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jreader.dao.FeedEntryDao;
import jreader.dao.FeedEntryFilter;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;
import jreader.dto.FeedEntryDto;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryService;

import org.springframework.core.convert.ConversionService;

public class FeedEntryServiceImpl extends AbstractService implements FeedEntryService {

    private FeedEntryDao feedEntryDao;

    private ConversionService conversionService;

    public FeedEntryServiceImpl(final UserDao userDao, final GroupDao groupDao, final SubscriptionDao subscriptionDao,
            final FeedEntryDao feedEntryDao, final ConversionService conversionService) {
        super(userDao, groupDao, subscriptionDao);
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
            final Group group = this.getGroup(user, groupEntry.getKey());
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
    public void star(final String username, final Long groupId, final Long subscriptionId, final Long feedEntryId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
        if (feedEntry != null && !feedEntry.isStarred()) {
            feedEntry.setStarred(true);
            feedEntryDao.save(feedEntry);
        }
    }

    @Override
    public void unstar(final String username, final Long groupId, final Long subscriptionId, final Long feedEntryId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
        if (feedEntry != null && feedEntry.isStarred()) {
            feedEntry.setStarred(false);
            feedEntryDao.save(feedEntry);
        }
    }

    @Override
    public List<FeedEntryDto> listEntries(final FeedEntryFilterData filterData) {
        switch (filterData.getType()) {
        case GROUP:
            return listEntriesForGroup(filterData.getUsername(), filterData.getGroupId(), filterData);
        case SUBSCRIPTION:
            return listSubscriptionEntries(filterData.getUsername(), filterData.getGroupId(), filterData.getSubscriptionId(), filterData);
        default:
            return listAllEntries(filterData.getUsername(), filterData);
        }
    }

    private List<FeedEntryDto> listAllEntries(final String username, final FeedEntryFilter filter) {
        final User user = this.getUser(username);
        return convert(feedEntryDao.list(user, filter));
    }

    private List<FeedEntryDto> listEntriesForGroup(final String username, final Long groupId, final FeedEntryFilter filter) {
        final User user = this.getUser(username);
        final Group group = getGroup(user, groupId);
        return convert(feedEntryDao.list(group, filter));
    }

    private List<FeedEntryDto> listSubscriptionEntries(final String username, final Long groupId, final Long subscriptionId,
            final FeedEntryFilter filter) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
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
