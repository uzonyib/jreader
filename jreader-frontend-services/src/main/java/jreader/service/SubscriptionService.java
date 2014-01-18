package jreader.service;

import java.util.List;

import jreader.dto.SubscriptionGroupDto;

public interface SubscriptionService {
	
	void createGroup(String username, String title);
	
	void subscribe(String username, Long subscriptionGroupId, String url);
	
	void unsubscribe(String username, Long subscriptionGroupId, Long subscriptionId);
	
	void deleteGroup(String username, Long subscriptionGroupId);
	
	void moveUp(String username, Long subscriptionGroupId);
	
	void moveDown(String username, Long subscriptionGroupId);
	
	List<SubscriptionGroupDto> list(String username);
	
	void entitle(String username, Long subscriptionGroupId, Long subscriptionId, String title);
	
	void entitle(String username, Long subscriptionGroupId, String title);

}
