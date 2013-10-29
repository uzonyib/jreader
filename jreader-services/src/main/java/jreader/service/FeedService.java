package jreader.service;

import java.util.List;

import jreader.dto.FeedEntryDto;

public interface FeedService {
	
	List<FeedEntryDto> listAllEntries(String username, boolean onlyUnread, boolean ascending);
	
	List<FeedEntryDto> listSubscriptionGroupEntries(String username, Long subscriptionGroupId, boolean onlyUnread, boolean ascending);
	
	List<FeedEntryDto> listSubscriptionEntries(String username, Long subscriptionGroupId, Long subscriptionId, boolean onlyUnread, boolean ascending);
	
	List<FeedEntryDto> listStarredEntries(String username, boolean onlyUnread, boolean ascending);
	
	void refreshFeeds();
	
	void cleanup(int olderThanDays, int keptCount);

}
