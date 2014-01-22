package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.dao.FeedEntryFilter;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.cmd.Query;

public class FeedEntryDaoImpl extends AbstractOfyDao<FeedEntry> implements FeedEntryDao {
	
	public FeedEntryDaoImpl(ObjectifyFactory objectifyFactory) {
		super(objectifyFactory);
	}

	@Override
	public FeedEntry find(Subscription subscription, Long id) {
		Objectify ofy = getOfy();
		return ofy.load().type(FeedEntry.class).parent(subscription).id(id).now();
	}
	
	@Override
	public FeedEntry find(Subscription subscription, int ordinal) {
		Objectify ofy = getOfy();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).order("-publishedDate").offset(ordinal - 1).limit(1).first().now();
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
		Objectify ofy = getOfy();
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
		Objectify ofy = getOfy();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).filter("starred", false).filter("publishedDate <", date).list();
	}
	
	@Override
	public List<FeedEntry> list(Subscription subscription) {
		Objectify ofy = getOfy();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).list();
	}
	
	@Override
	public int countUnread(Subscription subscription) {
		Objectify ofy = getOfy();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).filter("read", false).count();
	}

}
