package jreader.dao.impl;

import java.util.List;

import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public class SubscriptionDaoImpl extends AbstractOfyDao<Subscription> implements SubscriptionDao {

    @Override
    public Subscription find(final User user, final Feed feed) {
        return getOfy().load().type(Subscription.class).ancestor(user).filter("feedRef =", feed).first().now();
    }

    @Override
    public Subscription find(final SubscriptionGroup subscriptionGroup, final Long id) {
        return getOfy().load().type(Subscription.class).parent(subscriptionGroup).id(id).now();
    }

    @Override
    public List<Subscription> listSubscriptions(final Feed feed) {
        return getOfy().load().type(Subscription.class).filter("feedRef =", feed).list();
    }

    @Override
    public List<Subscription> list(final SubscriptionGroup group) {
        return getOfy().load().type(Subscription.class).ancestor(group).order("order").list();
    }

    @Override
    public int countSubscriptions(final SubscriptionGroup group) {
        return getOfy().load().type(Subscription.class).ancestor(group.getUser()).filter("groupRef =", group).count();
    }

    @Override
    public int countSubscribers(final Feed feed) {
        return getOfy().load().type(Subscription.class).filter("feedRef =", feed).count();
    }

    @Override
    public int getMaxOrder(final SubscriptionGroup group) {
        final Subscription subscription = getOfy().load().type(Subscription.class)
                .ancestor(group).order("-order").first().now();
        return subscription == null ? -1 : subscription.getOrder();
    }

}
