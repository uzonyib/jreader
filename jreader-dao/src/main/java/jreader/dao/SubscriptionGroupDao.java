package jreader.dao;

import java.util.Collection;
import java.util.List;

import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface SubscriptionGroupDao {
	
	SubscriptionGroup find(User user, Long id);
	
	SubscriptionGroup find(User user, String title);
	
	SubscriptionGroup save(SubscriptionGroup subscriptionGroup);
	
	void saveAll(Collection<SubscriptionGroup> subscriptionGroups);
	
	void delete(SubscriptionGroup group);
	
	int countSubscriptions(SubscriptionGroup group);
	
	int getMaxOrder(User user);
	
	List<SubscriptionGroup> list(User user);

}
