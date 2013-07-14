package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.User;

public interface FeedDao {
	
	Feed find(String id);
	
	Feed findByUrl(String url);
	
	List<Feed> listAll();
	
	void save(Feed feed);
	
	void delete(Feed feed);
	
	List<Feed> listFeedsFor(User user);
	
}
