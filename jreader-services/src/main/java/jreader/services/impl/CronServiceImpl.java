package jreader.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        final List<Subscription> subscriptions = subscriptionDao.listSubscriptions(feed);
        Integer statCounter = null;
        
        for (final Subscription subscription : subscriptions) {
            int counter = 0;
            final Long lastUpdatedDate = subscription.getUpdatedDate();
            Long newUpdatedDate = lastUpdatedDate;
            for (final FeedEntry feedEntry : rssFetchResult.getFeedEntries()) {
                if (feedEntry.getPublishedDate() == null) {
                    LOG.warning("Publish date is null. Feed: " + feed.getTitle());
                    continue;
                }
                if (lastUpdatedDate != null && feedEntry.getPublishedDate() < lastUpdatedDate) {
                    continue;
                }
                if (feedEntry.getUri() == null) {
                    LOG.warning("URI is null. Feed: " + feed.getTitle());
                    continue;
                }
                if (feedEntryDao.find(subscription, feedEntry.getUri()) == null) {
                    feedEntry.setSubscription(subscription);
                    feedEntryDao.save(feedEntry);
                    ++counter;
                    if (newUpdatedDate == null || feedEntry.getPublishedDate() > newUpdatedDate) {
                        newUpdatedDate = feedEntry.getPublishedDate();
                    }
                }
            }
            subscription.setUpdatedDate(newUpdatedDate);
            subscription.setRefreshDate(refreshDate);
            subscriptionDao.save(subscription);
            
            LOG.info("New items (" + subscription.getGroup().getUser().getUsername() + " - " + feed.getUrl() + "): " + counter);
            
            if (statCounter == null || statCounter > counter) {
                statCounter = counter;
            }
        }
        
        if (statCounter != null && statCounter > 0) {
            final FeedStat feedStat = builderFactory.createFeedStatBuilder().feed(feed).refreshDate(refreshDate).count(statCounter).build();
            feedStatDao.save(feedStat);
        }
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
        List<FeedStat> stats = feedStatDao.list(feed);
        feedStatDao.deleteAll(stats);
        LOG.info("Deleted stats (" + feed.getUrl() + "): " + stats.size());
    }

}
