package jreader.service.impl;

import jreader.dao.ActionDao;
import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.UserDao;
import jreader.domain.Action;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.User;
import jreader.service.ActionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionServiceImpl implements ActionService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FeedDao feedDao;
	
	@Autowired
	private FeedEntryDao feedEntryDao;
	
	@Autowired
	private ActionDao actionDao;

	@Override
	public void markRead(String username, String feedId, String feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		Feed feed = feedDao.find(feedId);
		if (feed == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(feed, feedEntryId);
		if (feedEntry == null) {
			return;
		}
		
		if (!actionDao.isRead(user, feedEntry)) {
			Action action = new Action();
			action.setUser(user);
			action.setFeedEntry(feedEntry);
			action.setType(ActionDao.READ_ACTION_TYPE);
			actionDao.save(action);
		}
	}
	
	@Override
	public void markStarred(String username, String feedId, String feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		Feed feed = feedDao.find(feedId);
		if (feed == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(feed, feedEntryId);
		if (feedEntry == null) {
			return;
		}
		
		if (!actionDao.isStarred(user, feedEntry)) {
			Action action = new Action();
			action.setUser(user);
			action.setFeedEntry(feedEntry);
			action.setType(ActionDao.STAR_ACTION_TYPE);
			actionDao.save(action);
		}
	}
	
	@Override
	public void unmarkStarred(String username, String feedId, String feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		Feed feed = feedDao.find(feedId);
		if (feed == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(feed, feedEntryId);
		if (feedEntry == null) {
			return;
		}
		
		Action action = actionDao.find(user, feedEntry, ActionDao.STAR_ACTION_TYPE);
		if (action != null) {
			actionDao.delete(action);
		}
	}

}
