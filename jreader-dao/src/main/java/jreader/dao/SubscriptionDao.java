package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface SubscriptionDao {
	
	Subscription save(Subscription subscription);
	
	Subscription find(User user, Feed feed);
	
	Subscription find(SubscriptionGroup subscriptionGroup, Long id);
	
	void delete(Subscription subscription);
	
	List<Subscription> listSubscriptions(Feed feed);
	
	int countSubscribers(Feed feed);
	
	List<Subscription> list(SubscriptionGroup group);
	
	int getMaxOrder(SubscriptionGroup group);
	
}
