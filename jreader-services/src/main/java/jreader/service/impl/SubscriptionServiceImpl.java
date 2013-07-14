package jreader.service.impl;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.rss.RssService;
import jreader.service.SubscriptionService;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SubscriptionDao subscriptionDao;
	
	@Autowired
	private FeedDao feedDao;
	
	@Autowired
	private FeedEntryDao feedEntryDao;
	
	@Autowired
	private RssService rssService;
	
	@Autowired
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
				feedEntryDao.save(feedEntry, feed);
			}
		}
		
		Subscription subscription = subscriptionDao.find(user, feed);
		if (subscription == null) {
			subscription = new Subscription();
			subscription.setTitle(feed.getTitle());
			subscriptionDao.save(user, feed, subscription);
		}
	}

	@Override
	public void unsubscribe(String username, String id) {
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
		}
		int subscriberCount = subscriptionDao.countSubscribers(feed);
		if (subscriberCount == 0) {
			feedDao.delete(feed);
		}
	}

}
