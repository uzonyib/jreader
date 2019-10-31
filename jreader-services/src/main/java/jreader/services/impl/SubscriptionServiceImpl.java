package jreader.services.impl;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import jreader.dao.DaoFacade;
import jreader.dao.FeedDao;
import jreader.dao.PostDao;
import jreader.domain.Feed;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.dto.RssFetchResult;
import jreader.dto.SubscriptionDto;
import jreader.services.RssService;
import jreader.services.SubscriptionService;
import jreader.services.exception.FetchException;
import jreader.services.exception.ResourceAlreadyExistsException;
import jreader.services.exception.ResourceNotFoundException;

@Service
public class SubscriptionServiceImpl extends AbstractService implements SubscriptionService {

    private FeedDao feedDao;
    private PostDao postDao;

    private RssService rssService;

    private ConversionService conversionService;

    @Autowired
    public SubscriptionServiceImpl(final DaoFacade daoFacade, final RssService rssService, final ConversionService conversionService) {
        super(daoFacade.getUserDao(), daoFacade.getGroupDao(), daoFacade.getSubscriptionDao());
        this.feedDao = daoFacade.getFeedDao();
        this.postDao = daoFacade.getPostDao();
        this.rssService = rssService;
        this.conversionService = conversionService;
    }

    @Override
    public SubscriptionDto subscribe(final String username, final Long groupId, final String url) {
        Feed feed = feedDao.find(url).orElseGet(() -> {
            final RssFetchResult rssFetchResult;
            try {
                rssFetchResult = rssService.fetch(url);
            } catch (FetchException e) {
                throw new IllegalArgumentException("Feed '" + url + "' cannot be fetched.", e);
            }
            return feedDao.save(rssFetchResult.getFeed());
        });

        final User user = this.getUser(username);
        if (subscriptionDao.find(user, feed).isPresent()) {
            throw new ResourceAlreadyExistsException("User " + username + " is already subscribed to " + url + ".");
        }

        final Group group = this.getGroup(user, groupId);
        Subscription subscription = Subscription.builder().group(group).feed(feed).title(feed.getTitle()).order(subscriptionDao.getMaxOrder(group) + 1).build();
        subscription = subscriptionDao.save(subscription);

        return conversionService.convert(subscription, SubscriptionDto.class);
    }

    @Override
    public void unsubscribe(final String username, final Long groupId, final Long subscriptionId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final Subscription subscription = this.getSubscription(group, subscriptionId);

        final List<Post> posts = postDao.list(subscription);
        postDao.deleteAll(posts);
        subscriptionDao.delete(subscription);
    }
    
    @Override
    public void entitle(final String username, final Long groupId, final Long subscriptionId, final String title) {
        Assert.hasLength(title, "Subscription title invalid.");

        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        if (subscriptionDao.find(group, title).isPresent()) {
            throw new ResourceAlreadyExistsException("Subscription with title '" + title + "' in the same group already exists.");
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

        int index = findSubscription(subscriptionId, subscriptions);
        Assert.isTrue(index > 0, "Cannot move first subscription up.");

        swap(subscriptions.get(index - 1), subscriptions.get(index));
    }

    @Override
    public void moveDown(final String username, final Long groupId, final Long subscriptionId) {
        final User user = this.getUser(username);
        final Group group = this.getGroup(user, groupId);
        final List<Subscription> subscriptions = subscriptionDao.list(group);

        int index = findSubscription(subscriptionId, subscriptions);
        Assert.isTrue(index < subscriptions.size() - 1, "Cannot move last subscription down.");

        swap(subscriptions.get(index), subscriptions.get(index + 1));
    }

    private int findSubscription(final Long subscriptionId, final List<Subscription> subscriptions) {
        Integer subscriptionIndex = null;
        for (int i = 0; i < subscriptions.size(); ++i) {
            if (subscriptions.get(i).getId().equals(subscriptionId)) {
                subscriptionIndex = i;
            }
        }

        if (isNull(subscriptionIndex)) {
            throw new ResourceNotFoundException("Subscription not found, ID: " + subscriptionId);
        }

        return subscriptionIndex;
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
