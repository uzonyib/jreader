package jreader.services;

import java.util.List;

import jreader.dto.FeedDto;

public interface CronService {
    
    List<FeedDto> listAll();
	
	void refreshFeed(String url);
	
	void cleanup(int olderThanDays, int keptCount);

}
