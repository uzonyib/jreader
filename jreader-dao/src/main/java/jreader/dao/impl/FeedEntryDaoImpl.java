package jreader.dao.impl;

import java.util.List;

import jreader.common.FeedEntryFilter;
import jreader.dao.FeedEntryDao;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;

public class FeedEntryDaoImpl implements FeedEntryDao {
	
	private ObjectifyFactory objectifyFactory;
	
	public FeedEntryDaoImpl(ObjectifyFactory objectifyFactory) {
		this.objectifyFactory = objectifyFactory;
	}

	@Override
	public FeedEntry find(Subscription subscription, Long id) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).parent(subscription).id(id).now();
	}
	
	@Override
	public FeedEntry find(Subscription subscription, int ordinal) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).order("-publishedDate").offset(ordinal - 1).limit(1).first().now();
	}
	
	@Override
	public FeedEntry save(final FeedEntry feedEntry) {
		final Objectify ofy = objectifyFactory.begin();
		return ofy.transact(new Work<FeedEntry>() {
			@Override
			public FeedEntry run() {
				Key<FeedEntry> key = ofy.save().entity(feedEntry).now();
				return ofy.load().key(key).now();
			}
		});
	}
	
	@Override
	public void saveAll(final List<FeedEntry> feedEntries) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entities(feedEntries).now();
			}
		});
	}
	
	@Override
	public void delete(final FeedEntry feedEntry) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.delete().entity(feedEntry).now();
			}
		});
	}
	
	@Override
	public List<FeedEntry> list(User user, FeedEntryFilter filter) {
		return listForAncestor(user, filter);
	}
	
	@Override
	public List<FeedEntry> list(SubscriptionGroup subscriptionGroup, FeedEntryFilter filter) {
		return listForAncestor(subscriptionGroup, filter);
	}
	
	@Override
	public List<FeedEntry> list(Subscription subscription, FeedEntryFilter filter) {
		return listForAncestor(subscription, filter);
	}
	
	private List<FeedEntry> listForAncestor(Object ancestor, FeedEntryFilter filter) {
		Objectify ofy = objectifyFactory.begin();
		Query<FeedEntry> query = ofy.load().type(FeedEntry.class).ancestor(ancestor);
		switch (filter.getSelection()) {
			case UNREAD:
				query = query.filter("read", false);
				break;
			case STARRED:
				query = query.filter("starred", true);
				break;
			default:
				break;
		}
		return query.order(filter.isAscending() ? "publishedDate" : "-publishedDate").offset(filter.getOffset()).limit(filter.getCount()).list();
	}
	
	@Override
	public List<FeedEntry> listUnstarredOlderThan(Subscription subscription, long date) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).filter("starred", false).filter("publishedDate <", date).list();
	}
	
	@Override
	public int countUnread(Subscription subscription) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).filter("read", false).count();
	}

}
