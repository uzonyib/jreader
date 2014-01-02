package jreader.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jreader.common.FeedEntryFilter;
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
import jreader.dto.FeedEntryDto;
import jreader.rss.RssService;
import jreader.service.FeedEntryFilterData;
import jreader.service.FeedService;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FeedServiceImpl implements FeedService {
	
	private static final Logger LOG = Logger.getLogger(FeedServiceImpl.class.getName());
	
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
	public List<FeedEntryDto> listEntries(FeedEntryFilterData filterData) {
		switch (filterData.getGroup()) {
		case SUBSCRIPTION_GROUP:
			return listSubscriptionGroupEntries(filterData.getUsername(), filterData.getSubscriptionGroupId(), filterData);
		case SUBSCRIPTION:
			return listSubscriptionEntries(filterData.getUsername(), filterData.getSubscriptionGroupId(), filterData.getSubscriptionId(), filterData);
		default:
			return listAllEntries(filterData.getUsername(), filterData);
		}
	}

	private List<FeedEntryDto> listAllEntries(String username, FeedEntryFilter filter) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		
		return convert(user, feedEntryDao.list(user, filter));
	}
	
	private List<FeedEntryDto> listSubscriptionGroupEntries(String username, Long subscriptionGroupId, FeedEntryFilter filter) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return Collections.emptyList();
		}
		
		return convert(user, feedEntryDao.list(subscriptionGroup, filter));
	}
	
	private List<FeedEntryDto> listSubscriptionEntries(String username, Long subscriptionGroupId, Long subscriptionId, FeedEntryFilter filter) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return Collections.emptyList();
		}
		
		Subscription subscription = subscriptionDao.find(subscriptionGroup, subscriptionId);
		if (subscription == null) {
			return Collections.emptyList();
		}
		
		return convert(user, feedEntryDao.list(subscription, filter));
	}

	private List<FeedEntryDto> convert(User user, List<FeedEntry> starredEntries) {
		List<FeedEntryDto> dtos = new ArrayList<FeedEntryDto>();
		for (FeedEntry starredEntry : starredEntries) {
			dtos.add(mapper.map(starredEntry, FeedEntryDto.class));
		}
		return dtos;
	}

	@Override
	public void refreshFeeds() {
		List<Feed> feeds = feedDao.listAll();
		for (Feed feed : feeds) {
			jreader.rss.domain.Feed rssFeed = rssService.fetch(feed.getUrl());
			if (rssFeed == null) {
				continue;
			}
			long refreshDate = System.currentTimeMillis();
			
			List<Subscription> subscriptions = subscriptionDao.listSubscriptions(feed);
			
			for (Subscription subscription : subscriptions) {
				int counter = 0;
				Long updatedDate = subscription.getUpdatedDate();
				for (jreader.rss.domain.FeedEntry rssFeedEntry : rssFeed.getEntries()) {
					if (rssFeedEntry.getPublishedDate() > subscription.getUpdatedDate()) {
						FeedEntry feedEntry = mapper.map(rssFeedEntry, FeedEntry.class);
						feedEntry.setSubscription(subscription);
						feedEntryDao.save(feedEntry);
						++counter;
						if (rssFeedEntry.getPublishedDate() > updatedDate) {
							updatedDate = rssFeedEntry.getPublishedDate();
						}
					}
				}
				subscription.setUpdatedDate(updatedDate);
				subscription.setRefreshDate(refreshDate);
				subscriptionDao.save(subscription);
				LOG.info(feed.getTitle() + " new items for user " + subscription.getGroup().getUser().getUsername() + ": " + counter);
			}
		}
	}
	
	@Override
	public void cleanup(int olderThanDays, int keptCount) {
		List<Feed> feeds = feedDao.listAll();
		long date = new Date().getTime() - 1000 * 60 * 60 * 24 * olderThanDays;
		for (Feed feed : feeds) {
			for (Subscription subscription : subscriptionDao.listSubscriptions(feed)) {
				int count = 0;
				FeedEntry e = feedEntryDao.find(subscription, keptCount);
				if (e != null) {
					long threshold = Math.min(date, e.getPublishedDate());
					List<FeedEntry> feedEntries = feedEntryDao.listUnstarredOlderThan(subscription, threshold);
					for (FeedEntry feedEntry : feedEntries) {
						feedEntryDao.delete(feedEntry);
						++count;
					}
					LOG.info(feed.getTitle() + "(" + subscription.getGroup().getUser().getUsername() + ") deleted items older than " + new Date(threshold) + ": " + count);
				}
			}
		}
	}

}
