package jreader.dao;

import java.util.List;

import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface FeedEntryDao {
	
	FeedEntry find(Subscription subscription, Long id);
	
	FeedEntry find(Subscription subscription, int ordinal);
	
	FeedEntry save(FeedEntry feedEntry);
	
	void saveAll(List<FeedEntry> feedEntries);
	
	void delete(FeedEntry feedEntry);
	
	List<FeedEntry> list(User user, boolean onlyUnread, boolean ascending);
	
	List<FeedEntry> list(SubscriptionGroup subscriptionGroup, boolean onlyUnread, boolean ascending);
	
	List<FeedEntry> list(Subscription subscription, boolean onlyUnread, boolean ascending);
	
	List<FeedEntry> listStarred(User user, boolean onlyUnread, boolean ascending);
	
	List<FeedEntry> listUnstarredOlderThan(Subscription subscription, long date);
	
	int countUnread(Subscription subscription);

}
