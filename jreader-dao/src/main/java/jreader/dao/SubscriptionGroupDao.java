package jreader.dao;

import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface SubscriptionGroupDao {
	
	SubscriptionGroup find(User user, String title);
	
	void save(SubscriptionGroup subscriptionGroup, User parent);
	
	void delete(SubscriptionGroup group);
	
	int countSubscriptions(SubscriptionGroup group, User user);

}
