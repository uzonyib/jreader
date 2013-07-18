package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.User;

public interface FeedEntryDao {
	
	FeedEntry find(Long id);
	
	FeedEntry find(User user, Feed feed, int ordinal);
	
	void save(FeedEntry feedEntry);
	
	void delete(FeedEntry feedEntry);
	
	List<FeedEntry> listEntriesByIds(List<Long> feedEntryIds);
	
	List<FeedEntry> listEntries(User user, List<Long> feedIds, boolean onlyUnread, boolean ascending);
	
	List<FeedEntry> listStarredEntries(User user, boolean onlyUnread, boolean ascending);
	
	List<FeedEntry> listUnstarredEntriesOlderThan(User user, Feed feed, long date);
	
	int countUnread(User user, Feed feed);

}
