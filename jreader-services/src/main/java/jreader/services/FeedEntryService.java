package jreader.services;

import java.util.List;

import jreader.dto.FeedEntryDto;

public interface FeedEntryService {
	
	void markRead(String username, List<Long> subscriptionGroupIds, List<Long> subscriptionIds, List<Long> feedEntryIds);
	
	void star(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId);
	
	void unstar(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId);
	
	List<FeedEntryDto> listEntries(FeedEntryFilterData filterData);
	
}
