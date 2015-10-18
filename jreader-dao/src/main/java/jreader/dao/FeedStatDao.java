package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.FeedStat;

public interface FeedStatDao extends OfyDao<FeedStat> {
    
    List<FeedStat> list(Feed feed);
    
    List<FeedStat> list(Feed feed, long dateAfter);

}
