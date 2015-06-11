package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.RssFetchResult;
import jreader.dto.SubscriptionDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.RssService;
import jreader.services.ServiceException;
import jreader.services.ServiceStatus;
import jreader.services.SubscriptionService;

import org.springframework.core.convert.ConversionService;

public class SubscriptionServiceImpl extends AbstractService implements SubscriptionService {

    private FeedDao feedDao;
    private FeedEntryDao feedEntryDao;

    private RssService rssService;

    private ConversionService conversionService;

    private BuilderFactory builderFactory;

    public SubscriptionServiceImpl(final UserDao userDao, final SubscriptionGroupDao subscriptionGroupDao, final SubscriptionDao subscriptionDao,
            final FeedDao feedDao, final FeedEntryDao feedEntryDao, RssService rssService, final ConversionService conversionService,
            final BuilderFactory builderFactory) {
        super(userDao, subscriptionGroupDao, subscriptionDao);
        this.feedDao = feedDao;
        this.feedEntryDao = feedEntryDao;
        this.rssService = rssService;
        this.conversionService = conversionService;
        this.builderFactory = builderFactory;
    }

    @Override
    public SubscriptionGroupDto createGroup(final String username, final String title) {
        User user = this.getUser(username);
        if (subscriptionGroupDao.find(user, title) != null) {
            throw new ServiceException("Group already exists.", ServiceStatus.RESOURCE_ALREADY_EXISTS);
        }
        SubscriptionGroup group = subscriptionGroupDao.save(builderFactory.createGroupBuilder().user(user).title(title)
                .order(subscriptionGroupDao.getMaxOrder(user) + 1).build());
        return conversionService.convert(group, SubscriptionGroupDto.class);
    }

    @Override
    public SubscriptionDto subscribe(final String username, final Long subscriptionGroupId, final String url) {
        User user = this.getUser(username);
        SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);

        RssFetchResult rssFetchResult = rssService.fetch(url);
        if (rssFetchResult == null) {
            throw new ServiceException("Cannot fetch RSS: " + url, ServiceStatus.OTHER_ERROR);
        }
        long refreshDate = System.currentTimeMillis();
        Feed feed = feedDao.find(url);
        if (feed == null) {
            feed = feedDao.save(rssFetchResult.getFeed());
        }

        Subscription subscription = subscriptionDao.find(user, feed);
        if (subscription != null) {
            throw new ServiceException("Subscription already exists.", ServiceStatus.RESOURCE_ALREADY_EXISTS);
        }

        Long updatedDate = null;
        for (FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
            if (updatedDate == null || feedEntry.getPublishedDate() > updatedDate) {
                updatedDate = feedEntry.getPublishedDate();
            }
        }

        subscription = builderFactory.createSubscriptionBuilder().group(group).feed(feed).title(feed.getTitle()).order(subscriptionDao.getMaxOrder(group) + 1)
                .updatedDate(updatedDate).refreshDate(refreshDate).build();
        subscription = subscriptionDao.save(subscription);

        for (FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
            feedEntry.setSubscription(subscription);
            feedEntryDao.save(feedEntry);
        }

        // TODO create assignments for all subscribers

        return conversionService.convert(subscription, SubscriptionDto.class);
    }

    @Override
    public void unsubscribe(final String username, final Long subscriptionGroupId, final Long subscriptionId) {
        User user = this.getUser(username);
        SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        Subscription subscription = this.getSubscription(group, subscriptionId);

        List<FeedEntry> feedEntries = feedEntryDao.list(subscription);
        feedEntryDao.deleteAll(feedEntries);
        subscriptionDao.delete(subscription);
    }

    @Override
    public void deleteGroup(final String username, final Long subscriptionGroupId) {
        User user = this.getUser(username);
        SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);
        subscriptionGroupDao.delete(subscriptionGroup);
    }

    @Override
    public void moveUp(final String username, final Long subscriptionGroupId) {
        User user = this.getUser(username);

        List<SubscriptionGroup> subscriptionGroups = subscriptionGroupDao.list(user);
        Integer groupIndex = null;
        for (int i = 0; i < subscriptionGroups.size(); ++i) {
            if (subscriptionGroups.get(i).getId().equals(subscriptionGroupId)) {
                groupIndex = i;
            }
        }

        if (groupIndex == null || groupIndex == 0) {
            return;
        }

        swap(subscriptionGroups.get(groupIndex - 1), subscriptionGroups.get(groupIndex));
    }

    @Override
    public void moveDown(final String username, final Long subscriptionGroupId) {
        User user = this.getUser(username);

        List<SubscriptionGroup> subscriptionGroups = subscriptionGroupDao.list(user);
        Integer groupIndex = null;
        for (int i = 0; i < subscriptionGroups.size(); ++i) {
            if (subscriptionGroups.get(i).getId().equals(subscriptionGroupId)) {
                groupIndex = i;
            }
        }

        if (groupIndex == null || groupIndex == subscriptionGroups.size() - 1) {
            return;
        }

        swap(subscriptionGroups.get(groupIndex), subscriptionGroups.get(groupIndex + 1));
    }

    private void swap(final SubscriptionGroup group1, final SubscriptionGroup group2) {
        int order = group1.getOrder();
        group1.setOrder(group2.getOrder());
        group2.setOrder(order);

        List<SubscriptionGroup> updatedGroups = new ArrayList<SubscriptionGroup>();
        updatedGroups.add(group1);
        updatedGroups.add(group2);

        subscriptionGroupDao.saveAll(updatedGroups);
    }

    @Override
    public void moveUp(final String username, final Long subscriptionGroupId, final Long subscriptionId) {
        User user = this.getUser(username);
        SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);

        List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
        Integer subscriptionIndex = null;
        for (int i = 0; i < subscriptions.size(); ++i) {
            if (subscriptions.get(i).getId().equals(subscriptionId)) {
                subscriptionIndex = i;
            }
        }

        if (subscriptionIndex == null || subscriptionIndex == 0) {
            return;
        }

        swap(subscriptions.get(subscriptionIndex - 1), subscriptions.get(subscriptionIndex));
    }

    @Override
    public void moveDown(final String username, final Long subscriptionGroupId, final Long subscriptionId) {
        User user = this.getUser(username);
        SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);

        List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
        Integer subscriptionIndex = null;
        for (int i = 0; i < subscriptions.size(); ++i) {
            if (subscriptions.get(i).getId().equals(subscriptionId)) {
                subscriptionIndex = i;
            }
        }

        if (subscriptionIndex == null || subscriptionIndex == subscriptions.size() - 1) {
            return;
        }

        swap(subscriptions.get(subscriptionIndex), subscriptions.get(subscriptionIndex + 1));
    }

    private void swap(final Subscription subscription1, final Subscription subscription2) {
        int order = subscription1.getOrder();
        subscription1.setOrder(subscription2.getOrder());
        subscription2.setOrder(order);

        List<Subscription> updatedSubscriptions = new ArrayList<Subscription>();
        updatedSubscriptions.add(subscription1);
        updatedSubscriptions.add(subscription2);

        subscriptionDao.saveAll(updatedSubscriptions);
    }

    @Override
    public List<SubscriptionGroupDto> list(final String username) {
        User user = this.getUser(username);
        List<SubscriptionGroupDto> dtos = new ArrayList<SubscriptionGroupDto>();
        for (SubscriptionGroup subscriptionGroup : subscriptionGroupDao.list(user)) {
            SubscriptionGroupDto dto = conversionService.convert(subscriptionGroup, SubscriptionGroupDto.class);
            dto.setSubscriptions(new ArrayList<SubscriptionDto>());
            dtos.add(dto);
            List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
            int groupUnreadCount = 0;
            for (Subscription subscription : subscriptions) {
                SubscriptionDto subscriptionDto = conversionService.convert(subscription, SubscriptionDto.class);
                int unreadCount = feedEntryDao.countUnread(subscription);
                subscriptionDto.setUnreadCount(unreadCount);
                dto.getSubscriptions().add(subscriptionDto);
                groupUnreadCount += unreadCount;
            }
            dto.setUnreadCount(groupUnreadCount);
        }
        return dtos;
    }

    @Override
    public void entitle(final String username, final Long subscriptionGroupId, final Long subscriptionId, final String title) {
        User user = this.getUser(username);
        SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        Subscription subscription = this.getSubscription(group, subscriptionId);

        subscription.setTitle(title);
        subscriptionDao.save(subscription);
    }

    @Override
    public void entitle(final String username, final Long subscriptionGroupId, final String title) {
        User user = this.getUser(username);
        SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);

        subscriptionGroup.setTitle(title);
        subscriptionGroupDao.save(subscriptionGroup);
    }

}
