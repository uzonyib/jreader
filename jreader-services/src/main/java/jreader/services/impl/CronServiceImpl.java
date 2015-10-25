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
    private static final int STATUS_MIN = 0;
    private static final int STATUS_MAX = 5;

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
        
        final boolean failure = rssFetchResult == null;
        if (failure) {
            handleFailure(feed);
            return;
        }
        
        updateFeed(feed, refreshDate, rssFetchResult);

        final List<Subscription> subscriptions = subscriptionDao.listSubscriptions(feed);
        for (final Subscription subscription : subscriptions) {
            updateSubscription(feed, refreshDate, rssFetchResult, subscription);
        }
    }
    
    private void handleFailure(final Feed feed) {
        int status = feed.getStatus() == null ? STATUS_MIN : feed.getStatus();
        if (status < STATUS_MAX) {
            ++status;
        }
        feed.setStatus(status);
        feedDao.save(feed);
    }

    void updateFeed(final Feed feed, final long refreshDate, final RssFetchResult rssFetchResult) {
        Long updatedDate = feed.getUpdatedDate();
        final Map<Long, FeedStat> stats = new LinkedHashMap<Long, FeedStat>();
        for (final FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
            if (!isNew(feedEntry, feed)) {
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
            
            if (updatedDate == null || updatedDate < feedEntry.getPublishedDate()) {
                updatedDate = feedEntry.getPublishedDate();
            }
        }
        
        feed.setUpdatedDate(updatedDate);
        feed.setRefreshDate(refreshDate);
        int status = feed.getStatus() == null ? STATUS_MIN : feed.getStatus();
        if (status > STATUS_MIN) {
            --status;
        }
        feed.setStatus(status);
        feedDao.save(feed);
        
        if (stats.size() > 0) {
            feedStatDao.saveAll(new ArrayList<FeedStat>(stats.values()));
        }
    }
    
    boolean isNew(final FeedEntry entry, final Feed feed) {
        if (entry.getPublishedDate() == null) {
            LOG.warning("Published date is null. Feed: " + feed.getTitle());
            return false;
        }
        if (entry.getUri() == null) {
            LOG.warning("URI is null. Feed: " + feed.getTitle());
            return false;
        }
        if (feed.getUpdatedDate() != null && entry.getPublishedDate() <= feed.getUpdatedDate()) {
            return false;
        }
        if (feed.getUpdatedDate() == null && feed.getRefreshDate() != null && entry.getPublishedDate() <= feed.getRefreshDate()) {
            return false;
        }
        return true;
    }
    
    private void updateSubscription(final Feed feed, final long refreshDate, final RssFetchResult rssFetchResult, final Subscription subscription) {
        int counter = 0;
        Long newUpdatedDate = subscription.getUpdatedDate();
        for (final FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
            if (!isNew(feedEntry, subscription)) {
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
    
    boolean isNew(final FeedEntry feedEntry, final Subscription subscription) {
        if (feedEntry.getPublishedDate() == null || feedEntry.getUri() == null) {
            return false;
        }
        if (subscription.getUpdatedDate() != null && feedEntry.getPublishedDate() < subscription.getUpdatedDate()) {
            return false;
        }
        if (feedEntryDao.find(subscription, feedEntry.getUri(), feedEntry.getPublishedDate()) != null) {
            return false;
        }
        return true;
    }

    @Override
    public void cleanup(final String url, final int olderThanDays, final int keptCount, final int statsToKeep) {
        final long date = dateHelper.substractDaysFromCurrentDate(olderThanDays);
        final Feed feed = feedDao.find(url);
        final List<Subscription> subscriptions = subscriptionDao.listSubscriptions(feed);
        
        if (subscriptions.isEmpty()) {
            cleanupFeed(feed);
        } else {
            cleanupStats(feed, statsToKeep);
            
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
    }

    private void cleanupFeed(final Feed feed) {
        final List<FeedStat> stats = feedStatDao.list(feed);
        feedStatDao.deleteAll(stats);
        LOG.info("Deleted stats (" + feed.getUrl() + "): " + stats.size());
        
        feedDao.delete(feed);
        LOG.info("Deleted feed with no subscription: " + feed.getUrl());
    }
    
    private void cleanupStats(final Feed feed, final int statsToKeep) {
        final long threshold = dateHelper.substractDaysFromCurrentDate(statsToKeep);
        final List<FeedStat> stats = feedStatDao.listBefore(feed, threshold);
        feedStatDao.deleteAll(stats);
        LOG.info("Deleted stats older than " + new Date(threshold) + " (" + feed.getUrl() + "): " + stats.size());
    }

}
