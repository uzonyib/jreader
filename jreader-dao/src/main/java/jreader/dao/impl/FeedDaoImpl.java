package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;

public class FeedDaoImpl extends AbstractOfyDao<Feed> implements FeedDao {

    @Override
    public Feed find(final String url) {
        return getOfy().load().type(Feed.class).id(url).now();
    }

    @Override
    public List<Feed> listAll() {
        return getOfy().load().type(Feed.class).list();
    }

    @Override
    public Long getLastUpdatedDate(final Feed feed) {
        final FeedEntry last = getOfy().load().type(FeedEntry.class).filter("feedRef =", feed)
                .order("-publishedDate").first().now();
        return last == null ? null : last.getPublishedDate();
    }

}
