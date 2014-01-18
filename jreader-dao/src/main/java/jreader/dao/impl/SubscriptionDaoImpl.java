package jreader.dao.impl;

import java.util.Collection;
import java.util.List;

import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.QueryKeys;

public class SubscriptionDaoImpl implements SubscriptionDao {
	
	private ObjectifyFactory objectifyFactory;

	public SubscriptionDaoImpl(ObjectifyFactory objectifyFactory) {
		this.objectifyFactory = objectifyFactory;
	}

	@Override
	public Subscription save(final Subscription subscription) {
		final Objectify ofy = objectifyFactory.begin();
		return ofy.transact(new Work<Subscription>() {
			@Override
			public Subscription run() {
				Key<Subscription> key = ofy.save().entity(subscription).now();
				return ofy.load().key(key).now();
			}
		});
	}
	
	@Override
	public void saveAll(final Collection<Subscription> subscriptions) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entities(subscriptions).now();
			}
		});
	}
	
	@Override
	public Subscription find(User user, Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).ancestor(user).filter("feedRef =", feed).first().now();
	}
	
	@Override
	public Subscription find(SubscriptionGroup subscriptionGroup, Long id) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).parent(subscriptionGroup).id(id).now();
	}
	
	@Override
	public void delete(final Subscription subscription) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				QueryKeys<FeedEntry> feedEntryKeys = ofy.load().type(FeedEntry.class).ancestor(subscription).keys();
				ofy.delete().keys(feedEntryKeys).now();
				ofy.delete().entity(subscription).now();
			}
		});
	}
	
	@Override
	public List<Subscription> listSubscriptions(Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).filter("feedRef =", feed).list();
	}
	
	@Override
	public int countSubscribers(Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).filter("feedRef =", feed).count();
	}
	
	@Override
	public List<Subscription> list(SubscriptionGroup group) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).ancestor(group).order("order").list();
	}
	
	@Override
	public int getMaxOrder(SubscriptionGroup group) {
		Objectify ofy = objectifyFactory.begin();
		Subscription subscription = ofy.load().type(Subscription.class).ancestor(group).order("-order").first().now();
		return subscription == null ? -1 : subscription.getOrder();
	}

}
