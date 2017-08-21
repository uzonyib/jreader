package jreader.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

@Repository
public class SubscriptionDaoImpl extends AbstractOfyDao<Subscription> implements SubscriptionDao {

    @Override
    public Subscription find(final User user, final Feed feed) {
        return getOfy().load().type(Subscription.class).ancestor(user).filter("feedRef =", feed).first().now();
    }

    @Override
    public Subscription find(final Group group, final Long id) {
        return getOfy().load().type(Subscription.class).parent(group).id(id).now();
    }
    
    @Override
    public Subscription find(final Group group, final String title) {
        return getOfy().load().type(Subscription.class).ancestor(group).filter("title =", title).first().now();
    }

    @Override
    public List<Subscription> listSubscriptions(final Feed feed) {
        return getOfy().load().type(Subscription.class).filter("feedRef =", feed).list();
    }

    @Override
    public List<Subscription> list(final Group group) {
        return getOfy().load().type(Subscription.class).ancestor(group).order("order").list();
    }

    @Override
    public int countSubscribers(final Feed feed) {
        return getOfy().load().type(Subscription.class).filter("feedRef =", feed).count();
    }

    @Override
    public int getMaxOrder(final Group group) {
        final Subscription subscription = getOfy().load().type(Subscription.class)
                .ancestor(group).order("-order").first().now();
        return subscription == null ? -1 : subscription.getOrder();
    }

}
