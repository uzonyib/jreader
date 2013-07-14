package jreader.dao;

import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.User;

public interface SubscriptionDao {
	
	void save(User user, Feed feed, Subscription subscription);
	
	Subscription find(User user, Feed feed);
	
	void delete(Subscription subscription);
	
	int countSubscribers(Feed feed);
	
}
