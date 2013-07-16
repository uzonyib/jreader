package jreader.service;

import java.util.List;

import jreader.dto.SubscriptionGroupDto;

public interface SubscriptionService {
	
	void subscribe(String username, String url);
	
	void unsubscribe(String username, String id);
	
	void assign(String username, String feedId, String groupTitle);
	
	List<SubscriptionGroupDto> list(String username);
	
	void entitle(String username, String feedId, String subscriptionTitle);

}
