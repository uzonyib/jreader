package jreader.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;

import jreader.dao.FeedEntryDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;

@Repository
public class FeedEntryDaoImpl implements FeedEntryDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;
	
	@Override
	public FeedEntry find(Feed parent, String id) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).parent(parent).id(id).now();
	}
	
	@Override
	public FeedEntry findByLink(Feed parent, String link) {
		return find(parent, getId(link));
	}
	
	@Override
	public void save(final FeedEntry feedEntry, final Feed parent) {
		feedEntry.setId(getId(feedEntry.getLink()));
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				feedEntry.setFeed(Key.create(parent));
				ofy.save().entity(feedEntry).now();
			}
		});
	}

	@Override
	public List<FeedEntry> listEntries(Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(FeedEntry.class).ancestor(feed).order("-publishedDate").limit(10).list();
	}
	
	private static String getId(String link) {
		return "" + link.hashCode();
	}

}
