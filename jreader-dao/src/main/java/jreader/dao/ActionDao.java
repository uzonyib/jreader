package jreader.dao;

import java.util.List;

import jreader.domain.Action;
import jreader.domain.FeedEntry;
import jreader.domain.User;

public interface ActionDao {
	
	final String READ_ACTION_TYPE = "READ";
	
	void save(Action action);
	
	Action find(User user, FeedEntry feedEntry, String type);
	
	boolean isRead(User user, FeedEntry feedEntry);
	
	List<Action> list(User user, String type);

}
