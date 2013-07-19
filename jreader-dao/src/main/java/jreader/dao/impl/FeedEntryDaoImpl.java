package jreader.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
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
	public FeedEntry find(Long id) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).id(id).now();
	}
	
	@Override
	public FeedEntry find(User user, Feed feed, int ordinal) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).filter("userRef =", user).filter("feedRef =", feed).order("-publishedDate").offset(ordinal - 1).limit(1).first().now();
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
	public List<FeedEntry> listEntriesByIds(List<Long> feedEntryIds) {
		List<FeedEntry> entries = new ArrayList<FeedEntry>();
		for (Long feedEntryId : feedEntryIds) {
			entries.add(find(feedEntryId));
		}
		return entries;
	}

	@Override
	public List<FeedEntry> listEntries(User user, List<Long> feedIds, boolean onlyUnread, boolean ascending) {
		Objectify ofy = objectifyFactory.begin();
		Collection<Feed> values = ofy.load().type(Feed.class).ids(feedIds).values();
		Query<FeedEntry> query = ofy.load().type(FeedEntry.class).filter("userRef =", user).filter("feedRef in", values);
		if (onlyUnread) {
			query = query.filter("read", false);
		}
		return query.order(ascending ? "publishedDate" : "-publishedDate").limit(10).list();
	}
	
	@Override
	public List<FeedEntry> listStarredEntries(User user, boolean onlyUnread, boolean ascending) {
		Objectify ofy = objectifyFactory.begin();
		Query<FeedEntry> query = ofy.load().type(FeedEntry.class).filter("userRef =", user).filter("starred =", true);
		if (onlyUnread) {
			query = query.filter("read", false);
		}
		return query.order(ascending ? "publishedDate" : "-publishedDate").list();
	}
	
	@Override
	public List<FeedEntry> listUnstarredEntriesOlderThan(User user, Feed feed, long date) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).filter("userRef =", user).filter("feedRef =", feed).filter("starred =", false).filter("publishedDate <", date).list();
	}
	
	@Override
	public int countUnread(User user, Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).filter("userRef =", user).filter("feedRef =", feed).filter("read =", false).count();
	}

}
