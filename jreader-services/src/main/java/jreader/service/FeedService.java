package jreader.service;

import java.util.List;

import jreader.dto.FeedDto;
import jreader.dto.FeedEntryDto;

public interface FeedService {
	
	List<FeedDto> list();

	List<FeedDto> list(String username);
	
	List<FeedEntryDto> listEntries(String id);
	
	void refreshFeeds();

}
