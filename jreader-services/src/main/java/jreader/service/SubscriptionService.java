package jreader.service;

import java.util.List;

import jreader.dto.SubscriptionDto;

public interface SubscriptionService {
	
	void subscribe(String username, String url);
	
	void unsubscribe(String username, String id);
	
	void assign(String username, String feedId, String groupTitle);
	
	List<SubscriptionDto> list(String username);

}
