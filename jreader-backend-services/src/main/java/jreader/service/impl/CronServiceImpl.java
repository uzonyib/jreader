package jreader.service.impl;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.rss.RssService;
import jreader.service.CronService;

import org.dozer.Mapper;

public class CronServiceImpl implements CronService {
	
	private static final Logger LOG = Logger.getLogger(CronServiceImpl.class.getName());
	
	private SubscriptionDao subscriptionDao;
	private FeedDao feedDao;
	private FeedEntryDao feedEntryDao;
	
	private RssService rssService;
	
	private Mapper mapper;
	
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
					if (rssFeedEntry.getPublishedDate() == null) {
						LOG.warning("Publish date is null. Feed: " + feed.getTitle());
						continue;
					}
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

	public static Logger getLog() {
		return LOG;
	}

}
