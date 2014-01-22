package jreader.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.RssFetchResult;
import jreader.dto.SubscriptionDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.services.RssService;
import jreader.services.SubscriptionService;

import org.springframework.core.convert.ConversionService;

public class SubscriptionServiceImpl implements SubscriptionService {
	
	private UserDao userDao;
	private SubscriptionGroupDao subscriptionGroupDao;
	private SubscriptionDao subscriptionDao;
	private FeedDao feedDao;
	private FeedEntryDao feedEntryDao;

	private RssService rssService;
	
	private ConversionService conversionService;
	
	@Override
	public void createGroup(String username, String title) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		if (subscriptionGroupDao.find(user, title) == null) {
			SubscriptionGroup subscriptionGroup = new SubscriptionGroup();
			subscriptionGroup.setUser(user);
			subscriptionGroup.setTitle(title);
			subscriptionGroup.setOrder(subscriptionGroupDao.getMaxOrder(user) + 1);
			subscriptionGroupDao.save(subscriptionGroup);
		}
	}
	
	@Override
	public void subscribe(String username, Long subscriptionGroupId, String url) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroupId == null) {
			return;
		}
		
		RssFetchResult rssFetchResult = rssService.fetch(url);
		if (rssFetchResult == null) {
			return;
		}
		long refreshDate = System.currentTimeMillis();
		Feed feed = feedDao.find(url);
		if (feed == null) {
			feed = feedDao.save(rssFetchResult.getFeed());
		}
		
		Subscription subscription = subscriptionDao.find(user, feed);
		if (subscription != null) {
			return;
		}
		
		Long updatedDate = null;
		for (FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
			if (updatedDate == null || feedEntry.getPublishedDate() > updatedDate) {
				updatedDate = feedEntry.getPublishedDate();
			}
		}

		subscription = new Subscription();
		subscription.setTitle(feed.getTitle());
		subscription.setFeed(feed);
		subscription.setGroup(subscriptionGroup);
		subscription.setOrder(subscriptionDao.getMaxOrder(subscriptionGroup) + 1);
		subscription.setUpdatedDate(updatedDate);
		subscription.setRefreshDate(refreshDate);
		subscription = subscriptionDao.save(subscription);
		
		for (FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
			feedEntry.setSubscription(subscription);
			feedEntryDao.save(feedEntry);
		}
		
		// TODO create assignments for all subscribers
	}

	@Override
	public void unsubscribe(String username, Long subscriptionGroupId, Long subscriptionId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		
		Subscription subscription = subscriptionDao.find(subscriptionGroup, subscriptionId);
		if (subscription != null) {
			List<FeedEntry> feedEntries = feedEntryDao.list(subscription);
			feedEntryDao.deleteAll(feedEntries);
			subscriptionDao.delete(subscription);
		}
	}
	
	@Override
	public void deleteGroup(String username, Long subscriptionGroupId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		
		subscriptionGroupDao.delete(subscriptionGroup);
	}
	
	@Override
	public void moveUp(String username, Long subscriptionGroupId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		List<SubscriptionGroup> subscriptionGroups = subscriptionGroupDao.list(user);
		Integer groupIndex = null;
		for (int i = 0; i < subscriptionGroups.size(); ++i) {
			if (subscriptionGroups.get(i).getId().equals(subscriptionGroupId)) {
				groupIndex = i;
			}
		}
		
		if (groupIndex == null || groupIndex == 0) {
			return;
		}
		
		swap(subscriptionGroups.get(groupIndex - 1), subscriptionGroups.get(groupIndex));
	}
	
	@Override
	public void moveDown(String username, Long subscriptionGroupId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		List<SubscriptionGroup> subscriptionGroups = subscriptionGroupDao.list(user);
		Integer groupIndex = null;
		for (int i = 0; i < subscriptionGroups.size(); ++i) {
			if (subscriptionGroups.get(i).getId().equals(subscriptionGroupId)) {
				groupIndex = i;
			}
		}
		
		if (groupIndex == null || groupIndex == subscriptionGroups.size() - 1) {
			return;
		}
		
		swap(subscriptionGroups.get(groupIndex), subscriptionGroups.get(groupIndex + 1));
	}
	
	private void swap(SubscriptionGroup group1, SubscriptionGroup group2) {
		int order = group1.getOrder();
		group1.setOrder(group2.getOrder());
		group2.setOrder(order);
		
		List<SubscriptionGroup> updatedGroups = new ArrayList<SubscriptionGroup>();
		updatedGroups.add(group1);
		updatedGroups.add(group2);
		
		subscriptionGroupDao.saveAll(updatedGroups);
	}
	
	@Override
	public void moveUp(String username, Long subscriptionGroupId, Long subscriptionId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		
		List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
		Integer subscriptionIndex = null;
		for (int i = 0; i < subscriptions.size(); ++i) {
			if (subscriptions.get(i).getId().equals(subscriptionId)) {
				subscriptionIndex = i;
			}
		}
		
		if (subscriptionIndex == null || subscriptionIndex == 0) {
			return;
		}
		
		swap(subscriptions.get(subscriptionIndex - 1), subscriptions.get(subscriptionIndex));
	}
	
	@Override
	public void moveDown(String username, Long subscriptionGroupId, Long subscriptionId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		
		List<Subscription> subscriptions = subscriptionDao.list(subscriptionGroup);
		Integer subscriptionIndex = null;
		for (int i = 0; i < subscriptions.size(); ++i) {
			if (subscriptions.get(i).getId().equals(subscriptionId)) {
				subscriptionIndex = i;
			}
		}
		
		if (subscriptionIndex == null || subscriptionIndex == subscriptions.size() - 1) {
			return;
		}
		
		swap(subscriptions.get(subscriptionIndex), subscriptions.get(subscriptionIndex + 1));
	}
	
	private void swap(Subscription subscription1, Subscription subscription2) {
		int order = subscription1.getOrder();
		subscription1.setOrder(subscription2.getOrder());
		subscription2.setOrder(order);
		
		List<Subscription> updatedSubscriptions = new ArrayList<Subscription>();
		updatedSubscriptions.add(subscription1);
		updatedSubscriptions.add(subscription2);
		
		subscriptionDao.saveAll(updatedSubscriptions);
	}
	
	@Override
	public List<SubscriptionGroupDto> list(String username) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		List<SubscriptionGroupDto> dtos = new ArrayList<SubscriptionGroupDto>();
		for (SubscriptionGroup subscriptiongroup : subscriptionGroupDao.list(user)) {
			SubscriptionGroupDto dto = conversionService.convert(subscriptiongroup, SubscriptionGroupDto.class);
			dto.setSubscriptions(new ArrayList<SubscriptionDto>());
			dtos.add(dto);
			List<Subscription> subscriptions = subscriptionDao.list(subscriptiongroup);
			int groupUnreadCount = 0;
			for (Subscription subscription : subscriptions) {
				SubscriptionDto subscriptionDto = conversionService.convert(subscription, SubscriptionDto.class);
				int unreadCount = feedEntryDao.countUnread(subscription);
				subscriptionDto.setUnreadCount(unreadCount);
				dto.getSubscriptions().add(subscriptionDto);
				groupUnreadCount += unreadCount;
			}
			dto.setUnreadCount(groupUnreadCount);
		}
		return dtos;
	}
	
	@Override
	public void entitle(String username, Long subscriptionGroupId, Long subscriptionId, String title) {
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
		
		subscription.setTitle(title);
		subscriptionDao.save(subscription);
	}
	
	@Override
	public void entitle(String username, Long subscriptionGroupId, String title) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		
		subscriptionGroup.setTitle(title);
		subscriptionGroupDao.save(subscriptionGroup);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public SubscriptionGroupDao getSubscriptionGroupDao() {
		return subscriptionGroupDao;
	}

	public void setSubscriptionGroupDao(SubscriptionGroupDao subscriptionGroupDao) {
		this.subscriptionGroupDao = subscriptionGroupDao;
	}

	public SubscriptionDao getSubscriptionDao() {
		return subscriptionDao;
	}

	public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
		this.subscriptionDao = subscriptionDao;
	}

	public FeedDao getFeedDao() {
		return feedDao;
	}

	public void setFeedDao(FeedDao feedDao) {
		this.feedDao = feedDao;
	}

	public FeedEntryDao getFeedEntryDao() {
		return feedEntryDao;
	}

	public void setFeedEntryDao(FeedEntryDao feedEntryDao) {
		this.feedEntryDao = feedEntryDao;
	}

	public RssService getRssService() {
		return rssService;
	}

	public void setRssService(RssService rssService) {
		this.rssService = rssService;
	}

	public ConversionService getConversionService() {
		return conversionService;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

}
