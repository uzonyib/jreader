package jreader.service.impl;

import java.util.ArrayList;
import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.service.FeedEntryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedEntryServiceImpl implements FeedEntryService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SubscriptionGroupDao subscriptionGroupDao;
	
	@Autowired
	private SubscriptionDao subscriptionDao;
	
	@Autowired
	private FeedEntryDao feedEntryDao;

	@Override
	public void markRead(String username, List<Long> subscriptionGroupIds, List<Long> subscriptionIds, List<Long> feedEntryIds) {
		if (feedEntryIds == null || subscriptionIds == null || subscriptionGroupIds == null || feedEntryIds.size() != subscriptionIds.size() || feedEntryIds.size() != subscriptionGroupIds.size()) {
			return;
		}
		
		User user = userDao.find(username);
		if (user == null) {
			return;
		}

		List<FeedEntry> feedEntriesToSave = new ArrayList<FeedEntry>();
		for (int i = 0; i < feedEntryIds.size(); ++i) {
			SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupIds.get(i));
			if (subscriptionGroup == null) {
				continue;
			}
			Subscription subscription = subscriptionDao.find(subscriptionGroup, subscriptionIds.get(i));
			if (subscription == null) {
				continue;
			}
			FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryIds.get(i));
			if (feedEntry != null && !feedEntry.isRead()) {
				feedEntry.setRead(true);
				feedEntriesToSave.add(feedEntry);
			}
		}
		feedEntryDao.saveAll(feedEntriesToSave);
	}
	
	@Override
	public void star(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		Subscription subscription = subscriptionDao.find(subscriptionGroup, subscriptionId);
		if (subscription == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
		if (feedEntry != null && !feedEntry.isStarred()) {
			feedEntry.setStarred(true);
			feedEntryDao.save(feedEntry);
		}
	}
	
	@Override
	public void unstar(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		Subscription subscription = subscriptionDao.find(subscriptionGroup, subscriptionId);
		if (subscription == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
		if (feedEntry != null && feedEntry.isStarred()) {
			feedEntry.setStarred(false);
			feedEntryDao.save(feedEntry);
		}
	}

}
