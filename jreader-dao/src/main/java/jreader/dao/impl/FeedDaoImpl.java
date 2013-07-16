package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.cmd.QueryKeys;

@Repository
public class FeedDaoImpl implements FeedDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;
	
	@Override
	public Feed find(String id) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Feed.class).id(id).now();
	}
	
	@Override
	public Feed findByUrl(String url) {
		return find(getId(url));
	}

	@Override
	public List<Feed> listAll() {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Feed.class).list();
	}

	@Override
	public void save(final Feed feed) {
		feed.setId(getId(feed.getUrl()));
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entity(feed).now();
			}
		});
	}

	@Override
	public void delete(final Feed feed) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				QueryKeys<FeedEntry> feedEntryKeys = ofy.load().type(FeedEntry.class).ancestor(feed).keys();
				ofy.delete().keys(feedEntryKeys).now();
				ofy.delete().entity(feed).now();
			}
		});
	}
	
	private static String getId(String url) {
		return "" + url.hashCode();
	}

}
