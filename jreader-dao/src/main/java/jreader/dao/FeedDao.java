package jreader.dao;

import java.util.List;

import jreader.domain.Feed;

public interface FeedDao {
	
	Feed find(String url);
	
	List<Feed> listAll();
	
	Feed save(Feed feed);
	
	void delete(Feed feed);
	
	Long getLastUpdatedDate(Feed feed);
	
}
