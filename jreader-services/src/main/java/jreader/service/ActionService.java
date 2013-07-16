package jreader.service;

public interface ActionService {
	
	void markRead(String username, String feedId, String feedEntryId);
	
	void markStarred(String username, String feedId, String feedEntryId);
	
	void unmarkStarred(String username, String feedId, String feedEntryId);
	
}
