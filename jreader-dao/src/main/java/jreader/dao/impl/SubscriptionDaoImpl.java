package jreader.dao.impl;

import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.VoidWork;

@Repository
public class SubscriptionDaoImpl implements SubscriptionDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;

	@Override
	public void save(final User user, final Feed feed, final Subscription subscription) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				subscription.setUser(Key.create(user));
				subscription.setFeed(Ref.create(feed));
				ofy.save().entity(subscription).now();
			}
		});
	}
	
	@Override
	public Subscription find(User user, Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).ancestor(user).filter("feed =", feed).first().now();
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
	public int countSubscribers(Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).filter("feed =", feed).count();
	}

}
