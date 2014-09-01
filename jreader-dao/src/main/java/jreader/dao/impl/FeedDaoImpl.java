package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;

import com.googlecode.objectify.Objectify;

public class FeedDaoImpl extends AbstractOfyDao<Feed> implements FeedDao {

    @Override
    public Feed find(String url) {
        Objectify ofy = getOfy();
        return ofy.load().type(Feed.class).id(url).now();
    }

    @Override
    public List<Feed> listAll() {
        Objectify ofy = getOfy();
        return ofy.load().type(Feed.class).list();
    }

    @Override
    public Long getLastUpdatedDate(Feed feed) {
        Objectify ofy = getOfy();
        FeedEntry last = ofy.load().type(FeedEntry.class).filter("feedRef =", feed)
                .order("-publishedDate").first().now();
        return last == null ? null : last.getPublishedDate();
    }

}
