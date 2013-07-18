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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SubscriptionGroupDao subscriptionGroupDao;
	
	@Autowired
	private SubscriptionDao subscriptionDao;
	
	@Autowired
	private FeedDao feedDao;
	
	@Autowired
	private FeedEntryDao feedEntryDao;
	
	@Autowired
	private RssService rssService;
	
	@Autowired
	@Qualifier("servicesMapper")
	private Mapper mapper;
	
	@Override
	public void subscribe(String username, String url) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		Feed feed = feedDao.findByUrl(url);
		if (feed == null) {
			jreader.rss.domain.Feed rssFeed = rssService.fetch(url);
			if (rssFeed == null) {
				return;
			}
			feed = mapper.map(rssFeed, Feed.class);
			feedDao.save(feed);
			for (jreader.rss.domain.FeedEntry rssFeedEntry : rssFeed.getEntries()) {
				FeedEntry feedEntry = mapper.map(rssFeedEntry, FeedEntry.class);
				feedEntry.setFeed(feed);
				feedEntry.setUser(user);
				feedEntryDao.save(feedEntry);
			}
		}
		
		Subscription subscription = subscriptionDao.find(user, feed);
		if (subscription == null) {
			SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, null);
			if (subscriptionGroup == null) {
				subscriptionGroup = new SubscriptionGroup();
				subscriptionGroup.setUser(user);
				subscriptionGroup.setTitle(null);
				subscriptionGroup.setOrder(subscriptionGroupDao.getMaxOrder(user) + 1);
				subscriptionGroupDao.save(subscriptionGroup);
				subscriptionGroup = subscriptionGroupDao.find(user, null);
			}
			subscription = new Subscription();
			subscription.setTitle(feed.getTitle());
			subscription.setUser(user);
			subscription.setFeed(feed);
			subscription.setGroup(subscriptionGroup);
			subscription.setOrder(subscriptionDao.getMaxOrder(user, subscriptionGroup) + 1);
			subscriptionDao.save(subscription);
		}
	}

	@Override
	public void unsubscribe(String username, Long id) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		Feed feed = feedDao.find(id);
		if (feed == null) {
			return;
		}
		
		Subscription subscription = subscriptionDao.find(user, feed);
		if (subscription != null) {
			subscriptionDao.delete(subscription);
			SubscriptionGroup group = subscription.getGroup();
			if (group != null) {
				int subscriptionCount = subscriptionGroupDao.countSubscriptions(group, user);
				if (subscriptionCount == 0) {
					subscriptionGroupDao.delete(group);
				}
			}
		}

		int subscriberCount = subscriptionDao.countSubscribers(feed);
		if (subscriberCount == 0) {
			feedDao.delete(feed);
		}
	}
	
	@Override
	public void assign(String username, Long feedId, String groupTitle) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		Feed feed = feedDao.find(feedId);
		if (feed == null) {
			return;
		}
		
		Subscription subscription = subscriptionDao.find(user, feed);
		if (subscription == null) {
			return;
		}
		
		SubscriptionGroup group = subscriptionGroupDao.find(user, groupTitle);
		if (group == null) {
			group = new SubscriptionGroup();
			group.setTitle(groupTitle);
			group.setUser(user);
			group.setOrder(subscriptionGroupDao.getMaxOrder(user) + 1);
			subscriptionGroupDao.save(group);
			group = subscriptionGroupDao.find(user, groupTitle);
		}
		
		SubscriptionGroup prevGroup = subscription.getGroup();
		subscription.setGroup(group);
		subscription.setOrder(subscriptionDao.getMaxOrder(user, group) + 1);
		subscriptionDao.save(subscription);
		if (prevGroup != null) {
			int subscriptionCount = subscriptionGroupDao.countSubscriptions(prevGroup, user);
			if (subscriptionCount == 0) {
				subscriptionGroupDao.delete(prevGroup);
			}
		}
	}
	
	@Override
	public List<SubscriptionGroupDto> list(String username) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		List<SubscriptionGroupDto> dtos = new ArrayList<SubscriptionGroupDto>();
		for (SubscriptionGroup group : subscriptionGroupDao.list(user)) {
			SubscriptionGroupDto dto = mapper.map(group, SubscriptionGroupDto.class);
			dto.setSubscriptions(new ArrayList<SubscriptionDto>());
			dtos.add(dto);
			List<Subscription> subscriptions = subscriptionDao.list(user, group);
			int groupUnreadCount = 0;
			for (Subscription subscription : subscriptions) {
				SubscriptionDto subscriptionDto = mapper.map(subscription, SubscriptionDto.class);
				int unreadCount = feedEntryDao.countUnread(user, subscription.getFeed());
				subscriptionDto.setUnreadCount(unreadCount);
				dto.getSubscriptions().add(subscriptionDto);
				groupUnreadCount += unreadCount;
			}
			dto.setUnreadCount(groupUnreadCount);
		}
		return dtos;
	}
	
	@Override
	public void entitle(String username, Long feedId, String subscriptionTitle) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		Feed feed = feedDao.find(feedId);
		if (feed == null) {
			return;
		}
		
		Subscription subscription = subscriptionDao.find(user, feed);
		if (subscription == null) {
			return;
		}
		
		subscription.setTitle(subscriptionTitle);
		subscriptionDao.save(subscription);
	}

}
