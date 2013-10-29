package jreader.service;

import java.util.List;

public interface FeedEntryService {
	
	void markRead(String username, List<Long> subscriptionGroupIds, List<Long> subscriptionIds, List<Long> feedEntryIds);
	
	void star(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId);
	
	void unstar(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId);
	
}
