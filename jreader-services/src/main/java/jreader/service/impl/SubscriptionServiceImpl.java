package jreader.service.impl;

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
import jreader.dto.SubscriptionDto;
import jreader.dto.SubscriptionGroupDto;
import jreader.rss.RssService;
import jreader.service.SubscriptionService;

import org.dozer.Mapper;

public class SubscriptionServiceImpl implements SubscriptionService {
	
	private UserDao userDao;
	private SubscriptionGroupDao subscriptionGroupDao;
	private SubscriptionDao subscriptionDao;
	private FeedDao feedDao;
	private FeedEntryDao feedEntryDao;

	private RssService rssService;
	
	private Mapper mapper;
	
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
		
		jreader.rss.domain.Feed rssFeed = rssService.fetch(url);
		if (rssFeed == null) {
			return;
		}
		long refreshDate = System.currentTimeMillis();
		Feed feed = feedDao.find(url);
		if (feed == null) {
			feed = mapper.map(rssFeed, Feed.class);
		}
		feed = feedDao.save(feed);
		
		Subscription subscription = subscriptionDao.find(user, feed);
		if (subscription != null) {
			return;
		}
		
		Long updatedDate = null;
		for (jreader.rss.domain.FeedEntry rssFeedEntry : rssFeed.getEntries()) {
			if (updatedDate == null || rssFeedEntry.getPublishedDate() > updatedDate) {
				updatedDate = rssFeedEntry.getPublishedDate();
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
		
		for (jreader.rss.domain.FeedEntry rssFeedEntry : rssFeed.getEntries()) {
			FeedEntry feedEntry = mapper.map(rssFeedEntry, FeedEntry.class);
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
			subscriptionDao.delete(subscription);
		}
	}
	
	@Override
	public List<SubscriptionGroupDto> list(String username) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		List<SubscriptionGroupDto> dtos = new ArrayList<SubscriptionGroupDto>();
		for (SubscriptionGroup subscriptiongroup : subscriptionGroupDao.list(user)) {
			SubscriptionGroupDto dto = mapper.map(subscriptiongroup, SubscriptionGroupDto.class);
			dto.setSubscriptions(new ArrayList<SubscriptionDto>());
			dtos.add(dto);
			List<Subscription> subscriptions = subscriptionDao.list(subscriptiongroup);
			int groupUnreadCount = 0;
			for (Subscription subscription : subscriptions) {
				SubscriptionDto subscriptionDto = mapper.map(subscription, SubscriptionDto.class);
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
	public void entitle(String username, Long subscriptionGroupId, Long subscriptionId, String subscriptionTitle) {
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
		
		subscription.setTitle(subscriptionTitle);
		subscriptionDao.save(subscription);
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

	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

}
