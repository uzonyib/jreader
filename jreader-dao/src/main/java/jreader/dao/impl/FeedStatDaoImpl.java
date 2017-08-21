package jreader.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import jreader.dao.FeedStatDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;

@Repository
public class FeedStatDaoImpl extends AbstractOfyDao<FeedStat> implements FeedStatDao {
    
    @Override
    public FeedStat find(final Feed feed, final long day) {
        return getOfy().load().type(FeedStat.class).ancestor(feed).filter("refreshDate =", day).first().now();
    }
    
    @Override
    public List<FeedStat> list(final Feed feed) {
        return getOfy().load().type(FeedStat.class).ancestor(feed).list();
    }

    @Override
    public List<FeedStat> listAfter(final Feed feed, final long dateAfter) {
        return getOfy().load().type(FeedStat.class).ancestor(feed).filter("refreshDate >=", dateAfter).list();
    }
    
    @Override
    public List<FeedStat> listBefore(final Feed feed, final long dateBefore) {
        return getOfy().load().type(FeedStat.class).ancestor(feed).filter("refreshDate <", dateBefore).list();
    }

}
