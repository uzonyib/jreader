package jreader.dao.impl;

import java.util.List;

import jreader.dao.SubscriptionGroupDao;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

public class SubscriptionGroupDaoImpl extends AbstractOfyDao<SubscriptionGroup> implements SubscriptionGroupDao {
	
	public SubscriptionGroupDaoImpl(ObjectifyFactory objectifyFactory) {
		super(objectifyFactory);
	}

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
	public int countSubscriptions(SubscriptionGroup group) {
		Objectify ofy = getOfy();
		return ofy.load().type(Subscription.class).ancestor(group.getUser()).filter("groupRef =", group).count();
	}
	
	@Override
	public int getMaxOrder(User user) {
		Objectify ofy = getOfy();
		SubscriptionGroup group = ofy.load().type(SubscriptionGroup.class).ancestor(user).order("-order").first().now();
		return group == null ? -1 : group.getOrder();
	}

}
