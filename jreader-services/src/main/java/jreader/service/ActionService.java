package jreader.service;

import java.util.List;

public interface ActionService {
	
	void markRead(String username, List<String> feedEntryIds);
	
	void star(String username, String feedEntryId);
	
	void unstar(String username, String feedEntryId);
	
}
