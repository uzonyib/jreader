package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.FeedEntry;

public interface FeedEntryDao {
	
	FeedEntry find(Feed parent, String id);
	
	FeedEntry findByLink(Feed parent, String link);
	
	void save(FeedEntry feedEntry);
	
	List<FeedEntry> listEntries(Feed feed);

}
