package jreader.dao.impl;

import java.util.List;

import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

public class SubscriptionDaoImpl extends AbstractOfyDao<Subscription> implements SubscriptionDao {
	
	public SubscriptionDaoImpl(ObjectifyFactory objectifyFactory) {
		super(objectifyFactory);
	}

	@Override
	public Subscription find(User user, Feed feed) {
		Objectify ofy = getOfy();
		return ofy.load().type(Subscription.class).ancestor(user).filter("feedRef =", feed).first().now();
	}
	
	@Override
	public Subscription find(SubscriptionGroup subscriptionGroup, Long id) {
		Objectify ofy = getOfy();
		return ofy.load().type(Subscription.class).parent(subscriptionGroup).id(id).now();
	}
	
	@Override
	public List<Subscription> listSubscriptions(Feed feed) {
		Objectify ofy = getOfy();
		return ofy.load().type(Subscription.class).filter("feedRef =", feed).list();
	}
	
	@Override
	public List<Subscription> list(SubscriptionGroup group) {
		Objectify ofy = getOfy();
		return ofy.load().type(Subscription.class).ancestor(group).order("order").list();
	}
	
	@Override
	public int countSubscribers(Feed feed) {
		Objectify ofy = getOfy();
		return ofy.load().type(Subscription.class).filter("feedRef =", feed).count();
	}
	
	@Override
	public int getMaxOrder(SubscriptionGroup group) {
		Objectify ofy = getOfy();
		Subscription subscription = ofy.load().type(Subscription.class).ancestor(group).order("-order").first().now();
		return subscription == null ? -1 : subscription.getOrder();
	}

}
