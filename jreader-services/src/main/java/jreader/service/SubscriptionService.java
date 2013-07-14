package jreader.service;

public interface SubscriptionService {
	
	void subscribe(String username, String url);
	
	void unsubscribe(String username, String id);

}
