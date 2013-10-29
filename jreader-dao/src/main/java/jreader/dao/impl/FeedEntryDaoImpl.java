package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.Query;

@Repository
public class FeedEntryDaoImpl implements FeedEntryDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;
	
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
	public List<FeedEntry> list(User user, boolean onlyUnread, boolean ascending) {
		return listForAncestor(user, onlyUnread, ascending);
	}
	
	@Override
	public List<FeedEntry> list(SubscriptionGroup subscriptionGroup, boolean onlyUnread, boolean ascending) {
		return listForAncestor(subscriptionGroup, onlyUnread, ascending);
	}
	
	@Override
	public List<FeedEntry> list(Subscription subscription, boolean onlyUnread, boolean ascending) {
		return listForAncestor(subscription, onlyUnread, ascending);
	}
	
	private List<FeedEntry> listForAncestor(Object ancestor, boolean onlyUnread, boolean ascending) {
		Objectify ofy = objectifyFactory.begin();
		Query<FeedEntry> query = ofy.load().type(FeedEntry.class).ancestor(ancestor);
		if (onlyUnread) {
			query = query.filter("read", false);
		}
		return query.order(ascending ? "publishedDate" : "-publishedDate").limit(10).list();
	}
	
	@Override
	public List<FeedEntry> listStarred(User user, boolean onlyUnread, boolean ascending) {
		Objectify ofy = objectifyFactory.begin();
		Query<FeedEntry> query = ofy.load().type(FeedEntry.class).ancestor(user).filter("starred =", true);
		if (onlyUnread) {
			query = query.filter("read", false);
		}
		return query.order(ascending ? "publishedDate" : "-publishedDate").list();
	}
	
	@Override
	public List<FeedEntry> listUnstarredOlderThan(Subscription subscription, long date) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).filter("starred =", false).filter("publishedDate <", date).list();
	}
	
	@Override
	public int countUnread(Subscription subscription) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).ancestor(subscription).filter("read =", false).count();
	}

}
