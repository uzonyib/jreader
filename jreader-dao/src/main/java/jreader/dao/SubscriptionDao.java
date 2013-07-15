package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface SubscriptionDao {
	
	void save(User user, Feed feed, Subscription subscription);
	
	void update(SubscriptionGroup group, Subscription subscription);
	
	Subscription find(User user, Feed feed);
	
	void delete(Subscription subscription);
	
	int countSubscribers(Feed feed);
	
	List<Subscription> list(User user);
	
}
