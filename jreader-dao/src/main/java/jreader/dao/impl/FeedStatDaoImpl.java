package jreader.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.cmd.LoadType;

import jreader.dao.FeedStatDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;

@Repository
public class FeedStatDaoImpl extends AbstractOfyDao<FeedStat> implements FeedStatDao {
    
    @Override
    protected LoadType<FeedStat> getLoadType() {
        return getOfy().load().type(FeedStat.class);
    }

    @Override
    public Optional<FeedStat> find(final Feed feed, final long day) {
        return Optional.ofNullable(getLoadType().ancestor(feed).filter("refreshDate =", day).first().now());
    }
    
    @Override
    public List<FeedStat> list(final Feed feed) {
        return getLoadType().ancestor(feed).list();
    }

    @Override
    public List<FeedStat> listAfter(final Feed feed, final long dateAfter) {
        return getLoadType().ancestor(feed).filter("refreshDate >=", dateAfter).list();
    }
    
    @Override
    public List<FeedStat> listBefore(final Feed feed, final long dateBefore) {
        return getLoadType().ancestor(feed).filter("refreshDate <", dateBefore).list();
    }

}
