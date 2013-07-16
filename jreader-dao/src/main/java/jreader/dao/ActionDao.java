package jreader.dao;

import java.util.List;

import jreader.domain.Action;
import jreader.domain.FeedEntry;
import jreader.domain.User;

public interface ActionDao {
	
	final String READ_ACTION_TYPE = "READ";
	final String STAR_ACTION_TYPE = "STAR";
	
	void save(Action action);
	
	void delete(Action action);
	
	Action find(User user, FeedEntry feedEntry, String type);
	
	boolean isRead(User user, FeedEntry feedEntry);
	
	boolean isStarred(User user, FeedEntry feedEntry);
	
	List<Action> list(User user, String type);

}
