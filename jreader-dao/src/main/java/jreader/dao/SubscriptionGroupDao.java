package jreader.dao;

import java.util.List;

import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface SubscriptionGroupDao {
	
	SubscriptionGroup find(User user, String title);
	
	void save(SubscriptionGroup subscriptionGroup);
	
	void delete(SubscriptionGroup group);
	
	int countSubscriptions(SubscriptionGroup group, User user);
	
	int getMaxOrder(User user);
	
	List<SubscriptionGroup> list(User user);

}
