package jreader.service;

import java.util.List;

public interface FeedEntryService {
	
	void markRead(String username, List<Long> feedEntryIds);
	
	void star(String username, Long feedEntryId);
	
	void unstar(String username, Long feedEntryId);
	
}
