package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;

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
import jreader.services.SubscriptionService;

public class SubscriptionServiceImpl extends AbstractService implements SubscriptionService {

    private FeedDao feedDao;
    private FeedEntryDao feedEntryDao;

    private RssService rssService;

    private ConversionService conversionService;

    private BuilderFactory builderFactory;

    public SubscriptionServiceImpl(final UserDao userDao, final SubscriptionGroupDao subscriptionGroupDao, final SubscriptionDao subscriptionDao,
            final FeedDao feedDao, final FeedEntryDao feedEntryDao, final RssService rssService, final ConversionService conversionService,
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
        if (title == null || "".equals(title)) {
            throw new ServiceException("Group title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (subscriptionGroupDao.find(user, title) != null) {
            throw new ServiceException("Group already exists.", HttpStatus.CONFLICT);
        }
        final SubscriptionGroup group = subscriptionGroupDao.save(builderFactory.createGroupBuilder().user(user).title(title)
                .order(subscriptionGroupDao.getMaxOrder(user) + 1).build());
        return conversionService.convert(group, SubscriptionGroupDto.class);
    }

    @Override
    public SubscriptionDto subscribe(final String username, final Long subscriptionGroupId, final String url) {
        final User user = this.getUser(username);
        final SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);

        final RssFetchResult rssFetchResult = rssService.fetch(url);
        if (rssFetchResult == null) {
            throw new ServiceException("Cannot fetch RSS: " + url, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        final long refreshDate = System.currentTimeMillis();
        Feed feed = feedDao.find(url);
        if (feed == null) {
            feed = feedDao.save(rssFetchResult.getFeed());
        }

        Subscription subscription = subscriptionDao.find(user, feed);
        if (subscription != null) {
            throw new ServiceException("Subscription already exists.", HttpStatus.CONFLICT);
        }

        Long updatedDate = null;
        for (final FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
            if (updatedDate == null || feedEntry.getPublishedDate() > updatedDate) {
                updatedDate = feedEntry.getPublishedDate();
            }
        }

        subscription = builderFactory.createSubscriptionBuilder().group(group).feed(feed).title(feed.getTitle()).order(subscriptionDao.getMaxOrder(group) + 1)
                .updatedDate(updatedDate).refreshDate(refreshDate).build();
        subscription = subscriptionDao.save(subscription);

        for (final FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
            feedEntry.setSubscription(subscription);
            feedEntryDao.save(feedEntry);
        }

        // TODO create assignments for all subscribers

        return conversionService.convert(subscription, SubscriptionDto.class);
    }

    @Override
    public void unsubscribe(final String username, final Long subscriptionGroupId, final Long subscriptionId) {
        final User user = this.getUser(username);
        final SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final List<FeedEntry> feedEntries = feedEntryDao.list(subscription);
        feedEntryDao.deleteAll(feedEntries);
        subscriptionDao.delete(subscription);
    }

    @Override
    public void deleteGroup(final String username, final Long subscriptionGroupId) {
        final User user = this.getUser(username);
        final SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);
        subscriptionGroupDao.delete(subscriptionGroup);
    }

    @Override
    public void moveUp(final String username, final Long subscriptionGroupId) {
        final User user = this.getUser(username);

        final List<SubscriptionGroup> subscriptionGroups = subscriptionGroupDao.list(user);
        Integer groupIndex = null;
        for (int i = 0; i < subscriptionGroups.size(); ++i) {
            if (subscriptionGroups.get(i).getId().equals(subscriptionGroupId)) {
                groupIndex = i;
            }
        }

        if (groupIndex == null) {
            throw new ServiceException("Group not found, ID: " + subscriptionGroupId, HttpStatus.NOT_FOUND);
        }
        if (groupIndex == 0) {
            throw new ServiceException("Cannot move first group up.", HttpStatus.BAD_REQUEST);
        }

        swap(subscriptionGroups.get(groupIndex - 1), subscriptionGroups.get(groupIndex));
    }

    @Override
    public void moveDown(final String username, final Long subscriptionGroupId) {
        final User user = this.getUser(username);

        final List<SubscriptionGroup> subscriptionGroups = subscriptionGroupDao.list(user);
        Integer groupIndex = null;
        for (int i = 0; i < subscriptionGroups.size(); ++i) {
            if (subscriptionGroups.get(i).getId().equals(subscriptionGroupId)) {
                groupIndex = i;
            }
        }

        if (groupIndex == null) {
            throw new ServiceException("Group not found, ID: " + subscriptionGroupId, HttpStatus.NOT_FOUND);
        }
        if (groupIndex == subscriptionGroups.size() - 1) {
            throw new ServiceException("Cannot move last group down.", HttpStatus.BAD_REQUEST);
        }

        swap(subscriptionGroups.get(groupIndex), subscriptionGroups.get(groupIndex + 1));
    }

    private void swap(final SubscriptionGroup group1, final SubscriptionGroup group2) {
        final int order = group1.getOrder();
        group1.setOrder(group2.getOrder());
        group2.setOrder(order);

        final List<SubscriptionGroup> updatedGroups = new ArrayList<SubscriptionGroup>();
        updatedGroups.add(group1);
        updatedGroups.add(group2);

        subscriptionGroupDao.saveAll(updatedGroups);
    }

    @Override
    public void moveUp(final String username, final Long subscriptionGroupId, final Long subscriptionId) {
        final User user = this.getUser(username);
        final SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);

        final List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
        Integer subscriptionIndex = null;
        for (int i = 0; i < subscriptions.size(); ++i) {
            if (subscriptions.get(i).getId().equals(subscriptionId)) {
                subscriptionIndex = i;
            }
        }

        if (subscriptionIndex == null) {
            throw new ServiceException("Subscription not found, ID: " + subscriptionId, HttpStatus.NOT_FOUND);
        }
        if (subscriptionIndex == 0) {
            throw new ServiceException("Cannot move first subscription up.", HttpStatus.BAD_REQUEST);
        }

        swap(subscriptions.get(subscriptionIndex - 1), subscriptions.get(subscriptionIndex));
    }

    @Override
    public void moveDown(final String username, final Long subscriptionGroupId, final Long subscriptionId) {
        final User user = this.getUser(username);
        final SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);

        final List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
        Integer subscriptionIndex = null;
        for (int i = 0; i < subscriptions.size(); ++i) {
            if (subscriptions.get(i).getId().equals(subscriptionId)) {
                subscriptionIndex = i;
            }
        }

        if (subscriptionIndex == null) {
            throw new ServiceException("Subscription not found, ID: " + subscriptionId, HttpStatus.NOT_FOUND);
        }
        if (subscriptionIndex == subscriptions.size() - 1) {
            throw new ServiceException("Cannot move last subscription down.", HttpStatus.BAD_REQUEST);
        }

        swap(subscriptions.get(subscriptionIndex), subscriptions.get(subscriptionIndex + 1));
    }

    private void swap(final Subscription subscription1, final Subscription subscription2) {
        final int order = subscription1.getOrder();
        subscription1.setOrder(subscription2.getOrder());
        subscription2.setOrder(order);

        final List<Subscription> updatedSubscriptions = new ArrayList<Subscription>();
        updatedSubscriptions.add(subscription1);
        updatedSubscriptions.add(subscription2);

        subscriptionDao.saveAll(updatedSubscriptions);
    }

    @Override
    public List<SubscriptionGroupDto> list(final String username) {
        final User user = this.getUser(username);
        final List<SubscriptionGroupDto> dtos = new ArrayList<SubscriptionGroupDto>();
        for (SubscriptionGroup subscriptionGroup : subscriptionGroupDao.list(user)) {
            final SubscriptionGroupDto dto = conversionService.convert(subscriptionGroup, SubscriptionGroupDto.class);
            dto.setSubscriptions(new ArrayList<SubscriptionDto>());
            dtos.add(dto);
            final List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
            int groupUnreadCount = 0;
            for (Subscription subscription : subscriptions) {
                final SubscriptionDto subscriptionDto = conversionService.convert(subscription, SubscriptionDto.class);
                final int unreadCount = feedEntryDao.countUnread(subscription);
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
        if (title == null || "".equals(title)) {
            throw new ServiceException("Subscription title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        final SubscriptionGroup group = this.getGroup(user, subscriptionGroupId);
        if (subscriptionDao.find(group, title) != null) {
            throw new ServiceException("Subscription with this title in the same group already exists.", HttpStatus.CONFLICT);
        }
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        subscription.setTitle(title);
        subscriptionDao.save(subscription);
    }

    @Override
    public void entitle(final String username, final Long subscriptionGroupId, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Group title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        if (subscriptionGroupDao.find(user, title) != null) {
            throw new ServiceException("Group with this title already exists.", HttpStatus.CONFLICT);
        }
        final SubscriptionGroup subscriptionGroup = this.getGroup(user, subscriptionGroupId);

        subscriptionGroup.setTitle(title);
        subscriptionGroupDao.save(subscriptionGroup);
    }

}
