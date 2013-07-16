package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.FeedEntry;

public interface FeedEntryDao {
	
	FeedEntry find(Feed parent, String id);
	
	FeedEntry find(Feed parent, int ordinal);
	
	FeedEntry findByLink(Feed parent, String link);
	
	void save(FeedEntry feedEntry);
	
	void delete(FeedEntry feedEntry);
	
	List<FeedEntry> listEntries(Feed feed);
	
	List<FeedEntry> listEntriesOlderThan(Feed feed, long date);

}
