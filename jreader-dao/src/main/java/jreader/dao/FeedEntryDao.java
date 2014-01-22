package jreader.dao;

import java.util.List;

import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface FeedEntryDao extends OfyDao<FeedEntry> {
	
	FeedEntry find(Subscription subscription, Long id);
	
	FeedEntry find(Subscription subscription, int ordinal);
	
	List<FeedEntry> list(User user, FeedEntryFilter filter);
	
	List<FeedEntry> list(SubscriptionGroup subscriptionGroup, FeedEntryFilter filter);
	
	List<FeedEntry> list(Subscription subscription, FeedEntryFilter filter);
	
	List<FeedEntry> listUnstarredOlderThan(Subscription subscription, long date);
	
	List<FeedEntry> list(Subscription subscription);
	
	int countUnread(Subscription subscription);

}
