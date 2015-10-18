package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedStatDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;

public class FeedStatDaoImpl extends AbstractOfyDao<FeedStat> implements FeedStatDao {
    
    @Override
    public List<FeedStat> list(final Feed feed) {
        return getOfy().load().type(FeedStat.class).ancestor(feed).list();
    }

    @Override
    public List<FeedStat> list(final Feed feed, final long dateAfter) {
        return getOfy().load().type(FeedStat.class).ancestor(feed).filter("refreshDate >=", dateAfter).list();
    }

}
