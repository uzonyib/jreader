package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import com.googlecode.objectify.cmd.QueryKeys;

@Repository
public class FeedDaoImpl implements FeedDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;
	
	@Override
	public Feed find(Long id) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Feed.class).id(id).now();
	}
	
	@Override
	public Feed findByUrl(String url) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Feed.class).filter("url =", url).first().now();
	}

	@Override
	public List<Feed> listAll() {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Feed.class).list();
	}

	@Override
	public Feed save(final Feed feed) {
		final Objectify ofy = objectifyFactory.begin();
		return ofy.transact(new Work<Feed>() {
			@Override
			public Feed run() {
				Key<Feed> key = ofy.save().entity(feed).now();
				return ofy.load().key(key).now();
			}
		});
	}

	@Override
	public void delete(final Feed feed) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				QueryKeys<FeedEntry> feedEntryKeys = ofy.load().type(FeedEntry.class).filter("feedRef =", feed).keys();
				ofy.delete().keys(feedEntryKeys).now();
				ofy.delete().entity(feed).now();
			}
		});
	}
	
	@Override
	public Long getLastUpdatedDate(Feed feed) {
		Objectify ofy = objectifyFactory.begin();
		FeedEntry last = ofy.load().type(FeedEntry.class).filter("feedRef =", feed).order("-publishedDate").first().now();
		return last == null ? null : last.getPublishedDate();
	}

}
