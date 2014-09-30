package jreader.dao.impl;

import jreader.domain.Archive;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public class EntityFactory {

    public SubscriptionGroup createGroup(final User user, final String title, final int order) {
        final SubscriptionGroup subscriptionGroup = new SubscriptionGroup();
        subscriptionGroup.setUser(user);
        subscriptionGroup.setTitle(title);
        subscriptionGroup.setOrder(order);
        return subscriptionGroup;
    }

    public Subscription createSubscription(final SubscriptionGroup group, final Feed feed,
            final String title, final int order, final Long updatedDate, final Long refreshDate) {
        final Subscription subscription = new Subscription();
        subscription.setGroup(group);
        subscription.setFeed(feed);
        subscription.setTitle(title);
        subscription.setOrder(order);
        subscription.setUpdatedDate(updatedDate);
        subscription.setRefreshDate(refreshDate);
        return subscription;
    }

    public Archive createArchive(final User user, final String title, final int order) {
        final Archive archive = new Archive();
        archive.setUser(user);
        archive.setTitle(title);
        archive.setOrder(order);
        return archive;
    }

}
