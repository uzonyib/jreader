package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedDao;
import jreader.domain.Feed;

public class FeedDaoImpl extends AbstractOfyDao<Feed> implements FeedDao {

    @Override
    public Feed find(final String url) {
        return getOfy().load().type(Feed.class).id(url).now();
    }

    @Override
    public List<Feed> listAll() {
        return getOfy().load().type(Feed.class).list();
    }

}
