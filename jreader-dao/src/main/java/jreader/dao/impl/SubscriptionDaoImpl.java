package jreader.dao.impl;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;

@Repository
public class SubscriptionDaoImpl implements SubscriptionDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;

	@Override
	public void save(final Subscription subscription) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entity(subscription).now();
			}
		});
	}
	
	@Override
	public Subscription find(User user, Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).filter("userRef =", user).filter("feedRef =", feed).first().now();
	}
	
	@Override
	public void delete(final Subscription subscription) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.delete().entity(subscription).now();
			}
		});
	}
	
	@Override
	public List<User> listSubscribers(Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		List<Subscription> subscriptions = ofy.load().type(Subscription.class).filter("feedRef =", feed).list();
		List<User> subscribers = new ArrayList<User>();
		for (Subscription subscription : subscriptions) {
			subscribers.add(subscription.getUser());
		}
		return subscribers;
	}
	
	@Override
	public int countSubscribers(Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).filter("feedRef =", feed).count();
	}
	
	@Override
	public List<Subscription> list(User user, SubscriptionGroup group) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).filter("userRef =", user).filter("groupRef =", group).order("order").list();
	}
	
	@Override
	public int getMaxOrder(User user, SubscriptionGroup group) {
		Objectify ofy = objectifyFactory.begin();
		Subscription subscription = ofy.load().type(Subscription.class).filter("userRef", user).filter("groupRef =", group).order("-order").first().now();
		return subscription == null ? -1 : subscription.getOrder();
	}

}
