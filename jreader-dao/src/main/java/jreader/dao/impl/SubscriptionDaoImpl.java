package jreader.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.cmd.LoadType;

import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

@Repository
public class SubscriptionDaoImpl extends AbstractOfyDao<Subscription> implements SubscriptionDao {

    @Override
    protected LoadType<Subscription> getLoadType() {
        return getOfy().load().type(Subscription.class);
    }

    @Override
    public Optional<Subscription> find(final User user, final Feed feed) {
        return Optional.ofNullable(getLoadType().ancestor(user).filter("feedRef =", feed).first().now());
    }

    @Override
    public Optional<Subscription> find(final Group group, final Long id) {
        return Optional.ofNullable(getLoadType().parent(group).id(id).now());
    }

    @Override
    public Optional<Subscription> find(final Group group, final String title) {
        return Optional.ofNullable(getLoadType().ancestor(group).filter("title =", title).first().now());
    }

    @Override
    public List<Subscription> listSubscriptions(final Feed feed) {
        return getLoadType().filter("feedRef =", feed).list();
    }

    @Override
    public List<Subscription> list(final Group group) {
        return getLoadType().ancestor(group).order("order").list();
    }

    @Override
    public int countSubscribers(final Feed feed) {
        return getLoadType().filter("feedRef =", feed).count();
    }

    @Override
    public int getMaxOrder(final Group group) {
        final Subscription subscription = getLoadType().ancestor(group).order("-order").first().now();
        return subscription == null ? -1 : subscription.getOrder();
    }

}
