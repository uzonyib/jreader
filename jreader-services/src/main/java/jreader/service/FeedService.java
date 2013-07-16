package jreader.service;

import java.util.List;

import jreader.dto.FeedDto;
import jreader.dto.FeedEntryDto;

public interface FeedService {
	
	List<FeedDto> list();

	List<FeedEntryDto> listEntries(String username, String feedId);
	
	List<FeedEntryDto> listStarredEntries(String username);
	
	void refreshFeeds();

}
