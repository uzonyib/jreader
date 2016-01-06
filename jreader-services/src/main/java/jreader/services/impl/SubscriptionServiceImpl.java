package jreader.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;
import jreader.dto.RssFetchResult;
import jreader.dto.SubscriptionDto;
import jreader.services.RssService;
import jreader.services.ServiceException;
import jreader.services.SubscriptionService;

public class SubscriptionServiceImpl extends AbstractService implements SubscriptionService {

    private FeedDao feedDao;
    private FeedEntryDao feedEntryDao;

    private RssService rssService;

    private ConversionService conversionService;

    private BuilderFactory builderFactory;

    public SubscriptionServiceImpl(final UserDao userDao, final GroupDao groupDao, final SubscriptionDao subscriptionDao,
            final FeedDao feedDao, final FeedEntryDao feedEntryDao, final RssService rssService, final ConversionService conversionService,
            final BuilderFactory builderFactory) {
        super(userDao, groupDao, subscriptionDao);
        this.feedDao = feedDao;
        this.feedEntryDao = feedEntryDao;
        this.rssService = rssService;
        this.conversionService = conversionService;
        this.builderFactory = builderFactory;
    }

    @Override
    public SubscriptionDto subscribe(final String username, final Long groupId, final String url) {
        Feed feed = feedDao.find(url);
        if (feed == null) {
            final RssFetchResult rssFetchResult = rssService.fetch(url);
            if (rssFetchResult == null) {
                throw new ServiceException("Cannot fetch feed: " + url, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            feed = feedDao.save(rssFetchResult.getFeed());
        }

        final User user = this.getUser(username);
        Subscription subscription = subscriptionDao.find(user, feed);
        if (subscription != null) {
            throw new ServiceException("Subscription already exists.", HttpStatus.CONFLICT);
        }

        final Group group = this.getGroup(user, groupId);
        subscription = builderFactory.createSubscriptionBuilder().group(group).feed(feed).title(feed.getTitle()).order(subscriptionDao.getMaxOrder(group) + 1)
                .build();
        subscription = subscriptionDao.save(subscription);

        return conversionService.convert(subscription, SubscriptionDto.class);
    }

    @Override
    public void unsubscribe(final String username, final Long groupId, final Long subscriptionId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final List<FeedEntry> feedEntries = feedEntryDao.list(subscription);
        feedEntryDao.deleteAll(feedEntries);
        subscriptionDao.delete(subscription);
    }
    
    @Override
    public void entitle(final String username, final Long groupId, final Long subscriptionId, final String title) {
        if (title == null || "".equals(title)) {
            throw new ServiceException("Subscription title invalid.", HttpStatus.BAD_REQUEST);
        }
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        if (subscriptionDao.find(group, title) != null) {
            throw new ServiceException("Subscription with this title in the same group already exists.", HttpStatus.CONFLICT);
        }
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        subscription.setTitle(title);
        subscriptionDao.save(subscription);
    }

    @Override
    public void moveUp(final String username, final Long groupId, final Long subscriptionId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);

        final List<Subscription> subscriptions = subscriptionDao.list(group);
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
    public void moveDown(final String username, final Long groupId, final Long subscriptionId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);

        final List<Subscription> subscriptions = subscriptionDao.list(group);
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

}
