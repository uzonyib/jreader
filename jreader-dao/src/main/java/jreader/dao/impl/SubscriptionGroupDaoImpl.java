package jreader.dao.impl;

import java.util.List;

import jreader.dao.SubscriptionGroupDao;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public class SubscriptionGroupDaoImpl extends AbstractOfyDao<SubscriptionGroup> implements SubscriptionGroupDao {

    @Override
    public SubscriptionGroup find(final User user, final Long id) {
        return getOfy().load().type(SubscriptionGroup.class).parent(user).id(id).now();
    }

    @Override
    public SubscriptionGroup find(final User user, final String title) {
        return getOfy().load().type(SubscriptionGroup.class).ancestor(user).filter("title =", title).first().now();
    }

    @Override
    public List<SubscriptionGroup> list(final User user) {
        return getOfy().load().type(SubscriptionGroup.class).ancestor(user).order("order").list();
    }

    @Override
    public int getMaxOrder(final User user) {
        final SubscriptionGroup group = getOfy().load().type(SubscriptionGroup.class).ancestor(user)
                .order("-order").first().now();
        return group == null ? -1 : group.getOrder();
    }

}
