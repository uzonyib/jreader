package jreader.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.core.convert.ConversionService;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.dto.FeedDto;
import jreader.dto.RssFetchResult;
import jreader.services.CronService;
import jreader.services.RssService;

public class CronServiceImpl implements CronService {
	
	private static final Logger LOG = Logger.getLogger(CronServiceImpl.class.getName());
	
	private SubscriptionDao subscriptionDao;
	private FeedDao feedDao;
	private FeedEntryDao feedEntryDao;
	
	private RssService rssService;
	private ConversionService conversionService;
	
	@Override
    public List<FeedDto> listAll() {
        List<Feed> feeds = feedDao.listAll();
        List<FeedDto> dtos = new ArrayList<FeedDto>();
        for (Feed feed : feeds) {
            dtos.add(conversionService.convert(feed, FeedDto.class));
        }
        return dtos;
    }
	
	@Override
	public void refreshFeed(String url) {
	    Feed feed = feedDao.find(url);
	    RssFetchResult rssFetchResult = rssService.fetch(feed.getUrl());
        if (rssFetchResult == null) {
            return;
        }
        
        long refreshDate = System.currentTimeMillis();
        List<Subscription> subscriptions = subscriptionDao.listSubscriptions(feed);
        
        for (Subscription subscription : subscriptions) {
            int counter = 0;
            Long updatedDate = subscription.getUpdatedDate();
            for (FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
                if (feedEntry.getPublishedDate() == null) {
                    LOG.warning("Publish date is null. Feed: " + feed.getTitle());
                    continue;
                }
                if (feedEntry.getPublishedDate() > subscription.getUpdatedDate()) {
                    feedEntry.setSubscription(subscription);
                    feedEntryDao.save(feedEntry);
                    ++counter;
                    if (feedEntry.getPublishedDate() > updatedDate) {
                        updatedDate = feedEntry.getPublishedDate();
                    }
                }
            }
            subscription.setUpdatedDate(updatedDate);
            subscription.setRefreshDate(refreshDate);
            subscriptionDao.save(subscription);
            LOG.info(feed.getTitle() + " new items for user " + subscription.getGroup().getUser().getUsername() + ": " + counter);
        }
	}
	
	@Override
	public void cleanup(int olderThanDays, int keptCount) {
		List<Feed> feeds = feedDao.listAll();
		long date = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * olderThanDays;
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

    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

}
