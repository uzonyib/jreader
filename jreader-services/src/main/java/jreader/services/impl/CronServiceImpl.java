package jreader.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.core.convert.ConversionService;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.FeedStatDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.BuilderFactory;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.FeedStat;
import jreader.domain.Subscription;
import jreader.dto.FeedDto;
import jreader.dto.RssFetchResult;
import jreader.services.CronService;
import jreader.services.DateHelper;
import jreader.services.RssService;

public class CronServiceImpl implements CronService {

    private static final Logger LOG = Logger.getLogger(CronServiceImpl.class.getName());

    private SubscriptionDao subscriptionDao;
    private FeedDao feedDao;
    private FeedEntryDao feedEntryDao;
    private FeedStatDao feedStatDao;

    private RssService rssService;
    private ConversionService conversionService;
    
    private BuilderFactory builderFactory;
    
    private DateHelper dateHelper;

    public CronServiceImpl(final SubscriptionDao subscriptionDao, final FeedDao feedDao, final FeedEntryDao feedEntryDao, final FeedStatDao feedStatDao,
            final RssService rssService, final ConversionService conversionService, final BuilderFactory builderFactory, final DateHelper dateHelper) {
        this.subscriptionDao = subscriptionDao;
        this.feedDao = feedDao;
        this.feedEntryDao = feedEntryDao;
        this.feedStatDao = feedStatDao;
        this.rssService = rssService;
        this.conversionService = conversionService;
        this.builderFactory = builderFactory;
        this.dateHelper = dateHelper;
    }

    @Override
    public List<FeedDto> listFeeds() {
        final List<Feed> feeds = feedDao.listAll();
        final List<FeedDto> dtos = new ArrayList<FeedDto>();
        for (final Feed feed : feeds) {
            dtos.add(conversionService.convert(feed, FeedDto.class));
        }
        return dtos;
    }

    @Override
    public void refresh(final String url) {
        final Feed feed = feedDao.find(url);
        final long refreshDate = dateHelper.getCurrentDate();
        final RssFetchResult rssFetchResult = rssService.fetch(feed.getUrl());
        if (rssFetchResult == null) {
            return;
        }
        
        updateFeed(feed, refreshDate, rssFetchResult);

        final List<Subscription> subscriptions = subscriptionDao.listSubscriptions(feed);
        
        for (final Subscription subscription : subscriptions) {
            updateSubscription(feed, refreshDate, rssFetchResult, subscription);
        }
        
    }

    void updateFeed(final Feed feed, final long refreshDate, final RssFetchResult rssFetchResult) {
        final Long lastRefreshDate = feed.getRefreshDate();
        feed.setRefreshDate(refreshDate);
        feedDao.save(feed);
        
        final Map<Long, FeedStat> stats = new LinkedHashMap<Long, FeedStat>();
        for (final FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
            if (!isNew(feedEntry, feed, lastRefreshDate)) {
                continue;
            }
            
            final long refreshDay = dateHelper.getFirstSecondOfDay(feedEntry.getPublishedDate());
            if (!stats.containsKey(refreshDay)) {
                FeedStat stat = feedStatDao.find(feed, refreshDay);
                if (stat == null) {
                    stat = builderFactory.createFeedStatBuilder().feed(feed).refreshDate(refreshDay).count(1).build();
                } else {
                    stat.setCount(stat.getCount() + 1);
                }
                stats.put(refreshDay, stat);
            } else {
                final FeedStat stat = stats.get(refreshDay);
                stat.setCount(stat.getCount() + 1);
            }
        }
        
        if (stats.size() > 0) {
            feedStatDao.saveAll(new ArrayList<FeedStat>(stats.values()));
        }
    }
    
    boolean isNew(final FeedEntry entry, final Feed feed, final Long feedRefreshDate) {
        if (entry.getPublishedDate() == null) {
            LOG.warning("Published date is null. Feed: " + feed.getTitle());
            return false;
        }
        if (entry.getUri() == null) {
            LOG.warning("URI is null. Feed: " + feed.getTitle());
            return false;
        }
        if (feedRefreshDate != null && entry.getPublishedDate() <= feedRefreshDate) {
            return false;
        }
        return true;
    }
    
    private void updateSubscription(final Feed feed, final long refreshDate, final RssFetchResult rssFetchResult, final Subscription subscription) {
        int counter = 0;
        final Long lastUpdatedDate = subscription.getUpdatedDate();
        Long newUpdatedDate = lastUpdatedDate;
        for (final FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
            if (!isNew(feedEntry, subscription, lastUpdatedDate)) {
                continue;
            }
            
            feedEntry.setSubscription(subscription);
            if (feedEntry.getPublishedDate() > refreshDate) {
                feedEntry.setPublishedDate(refreshDate);
            }
            feedEntryDao.save(feedEntry);
            ++counter;
            if (newUpdatedDate == null || feedEntry.getPublishedDate() > newUpdatedDate) {
                newUpdatedDate = feedEntry.getPublishedDate();
            }
        }
        
        subscription.setUpdatedDate(newUpdatedDate);
        subscription.setRefreshDate(refreshDate);
        subscriptionDao.save(subscription);
        
        LOG.info("New items (" + subscription.getGroup().getUser().getUsername() + " - " + feed.getUrl() + "): " + counter);
    }
    
    boolean isNew(final FeedEntry feedEntry, final Subscription subscription, final Long lastUpdatedDate) {
        if (feedEntry.getPublishedDate() == null || feedEntry.getUri() == null) {
            return false;
        }
        if (lastUpdatedDate != null && feedEntry.getPublishedDate() < lastUpdatedDate) {
            return false;
        }
        if (feedEntryDao.find(subscription, feedEntry.getUri()) != null) {
            return false;
        }
        return true;
    }

    @Override
    public void cleanup(final String url, final int olderThanDays, final int keptCount) {
        final long date = dateHelper.substractDaysFromCurrentDate(olderThanDays);
        final Feed feed = feedDao.find(url);
        final List<Subscription> subscriptions = subscriptionDao.listSubscriptions(feed);
        if (subscriptions.isEmpty()) {
            cleanupStats(feed);
            feedDao.delete(feed);
            LOG.info("Deleted feed with no subscription: " + feed.getUrl());
        }
        for (final Subscription subscription : subscriptions) {
            int count = 0;
            final FeedEntry e = feedEntryDao.find(subscription, keptCount);
            if (e != null) {
                final long threshold = Math.min(date, e.getPublishedDate());
                final List<FeedEntry> feedEntries = feedEntryDao.listUnstarredOlderThan(subscription, threshold);
                for (final FeedEntry feedEntry : feedEntries) {
                    feedEntryDao.delete(feedEntry);
                    ++count;
                }
                LOG.info("Deleted items older than " + new Date(threshold) + " (" + subscription.getGroup().getUser().getUsername() + " - " + feed.getUrl()
                        + "): " + count);
            }
        }
    }
    
    private void cleanupStats(final Feed feed) {
        final List<FeedStat> stats = feedStatDao.list(feed);
        feedStatDao.deleteAll(stats);
        LOG.info("Deleted stats (" + feed.getUrl() + "): " + stats.size());
    }

}
