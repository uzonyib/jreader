package jreader.dao.impl;

import java.util.List;

import jreader.dao.SubscriptionGroupDao;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import com.googlecode.objectify.Objectify;

public class SubscriptionGroupDaoImpl extends AbstractOfyDao<SubscriptionGroup> implements SubscriptionGroupDao {

    @Override
    public SubscriptionGroup find(User user, Long id) {
        Objectify ofy = getOfy();
        return ofy.load().type(SubscriptionGroup.class).parent(user).id(id).now();
    }

    @Override
    public SubscriptionGroup find(User user, String title) {
        Objectify ofy = getOfy();
        return ofy.load().type(SubscriptionGroup.class).ancestor(user).filter("title =", title).first().now();
    }

    @Override
    public List<SubscriptionGroup> list(User user) {
        Objectify ofy = getOfy();
        return ofy.load().type(SubscriptionGroup.class).ancestor(user).order("order").list();
    }

    @Override
    public int getMaxOrder(User user) {
        Objectify ofy = getOfy();
        SubscriptionGroup group = ofy.load().type(SubscriptionGroup.class).ancestor(user).order("-order").first().now();
        return group == null ? -1 : group.getOrder();
    }

}
