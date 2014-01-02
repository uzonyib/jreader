package jreader.service;

import java.util.List;

import jreader.dto.FeedEntryDto;

public interface FeedService {
	
	List<FeedEntryDto> listEntries(FeedEntryFilterData filterData);
	
	void refreshFeeds();
	
	void cleanup(int olderThanDays, int keptCount);

}
