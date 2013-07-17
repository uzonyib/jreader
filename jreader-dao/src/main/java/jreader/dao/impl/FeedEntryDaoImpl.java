package jreader.dao.impl;

import java.util.Collection;
import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;

@Repository
public class FeedEntryDaoImpl implements FeedEntryDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;
	
	@Override
	public FeedEntry find(String id) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).id(id).now();
	}
	
	@Override
	public FeedEntry find(Feed feed, int ordinal) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).filter("feedRef =", feed).order("-publishedDate").offset(ordinal - 1).limit(1).first().now();
	}
	
	@Override
	public FeedEntry findByLink(String link) {
		return find(getId(link));
	}
	
	@Override
	public void save(final FeedEntry feedEntry) {
		feedEntry.setId(getId(feedEntry.getLink()));
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entity(feedEntry).now();
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
	public List<FeedEntry> listEntries(List<String> feedIds) {
		Objectify ofy = objectifyFactory.begin();
		Collection<Feed> values = ofy.load().type(Feed.class).ids(feedIds).values();
		return ofy.load().type(FeedEntry.class).filter("feedRef in", values).order("-publishedDate").limit(10).list();
	}
	
	@Override
	public List<FeedEntry> listEntriesOlderThan(Feed feed, long date) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).filter("feedRef =", feed).filter("publishedDate <", date).list();
	}
	
	private static String getId(String link) {
		return "" + link.hashCode();
	}

}
