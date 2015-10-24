package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.FeedStat;

public interface FeedStatDao extends OfyDao<FeedStat> {
    
    FeedStat find(Feed feed, long day);
    
    List<FeedStat> list(Feed feed);
    
    List<FeedStat> listAfter(Feed feed, long dateAfter);
    
    List<FeedStat> listBefore(Feed feed, long dateBefore);

}
