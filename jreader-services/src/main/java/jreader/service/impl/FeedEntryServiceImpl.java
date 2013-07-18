package jreader.service.impl;

import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.dao.UserDao;
import jreader.domain.FeedEntry;
import jreader.domain.User;
import jreader.service.FeedEntryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedEntryServiceImpl implements FeedEntryService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FeedEntryDao feedEntryDao;

	@Override
	public void markRead(String username, List<Long> feedEntryIds) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		for (FeedEntry feedEntry : feedEntryDao.listEntriesByIds(feedEntryIds)) {
			if (user.getUsername().equals(feedEntry.getUser().getUsername()) && !feedEntry.isRead()) {
				feedEntry.setRead(true);
				feedEntryDao.save(feedEntry);
			}
		}
	}
	
	@Override
	public void star(String username, Long feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(feedEntryId);
		if (feedEntry == null) {
			return;
		}
		
		if (!feedEntry.isStarred()) {
			feedEntry.setStarred(true);
			feedEntryDao.save(feedEntry);
		}
	}
	
	@Override
	public void unstar(String username, Long feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(feedEntryId);
		if (feedEntry == null) {
			return;
		}
		
		if (feedEntry.isStarred()) {
			feedEntry.setStarred(false);
			feedEntryDao.save(feedEntry);
		}
	}

}
