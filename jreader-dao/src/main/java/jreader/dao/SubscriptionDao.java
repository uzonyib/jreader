package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface SubscriptionDao {
	
	Subscription save(Subscription subscription);
	
	Subscription find(User user, Feed feed);
	
	void delete(Subscription subscription);
	
	List<User> listSubscribers(Feed feed);
	
	int countSubscribers(Feed feed);
	
	List<Subscription> list(User user, SubscriptionGroup group);
	
	int getMaxOrder(User user, SubscriptionGroup group);
	
}
