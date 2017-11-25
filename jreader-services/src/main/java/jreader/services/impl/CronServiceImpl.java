package jreader.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

import jreader.dao.DaoFacade;
import jreader.dao.FeedDao;
import jreader.dao.FeedStatDao;
import jreader.dao.PostDao;
import jreader.dao.SubscriptionDao;
import jreader.domain.Feed;
import jreader.domain.FeedStat;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.dto.FeedDto;
import jreader.dto.RssFetchResult;
import jreader.services.CronService;
import jreader.services.DateHelper;
import jreader.services.RssService;
import jreader.services.exception.FetchException;

@Service
public class CronServiceImpl implements CronService {

    private static final Logger LOG = Logger.getLogger(CronServiceImpl.class.getName());
    private static final int STATUS_MIN = 0;
    private static final int STATUS_MAX = 5;

    private SubscriptionDao subscriptionDao;
    private FeedDao feedDao;
    private PostDao postDao;
    private FeedStatDao feedStatDao;

    private RssService rssService;
    private ConversionService conversionService;
    
    private DateHelper dateHelper;

    @Autowired
    public CronServiceImpl(final DaoFacade daoFacade, final RssService rssService, final ConversionService conversionService, final DateHelper dateHelper) {
        this.subscriptionDao = daoFacade.getSubscriptionDao();
        this.feedDao = daoFacade.getFeedDao();
        this.postDao = daoFacade.getPostDao();
        this.feedStatDao = daoFacade.getFeedStatDao();
        this.rssService = rssService;
        this.conversionService = conversionService;
        this.dateHelper = dateHelper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FeedDto> listFeeds() {
        return (List<FeedDto>) conversionService.convert(feedDao.listAll(),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Feed.class)),
                TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(FeedDto.class)));
    }

    @Override
    public void refresh(final String url) {
        final Feed feed = feedDao.find(url);
        final long refreshDate = dateHelper.getCurrentDate();
        final RssFetchResult rssFetchResult;

        try {
            rssFetchResult = rssService.fetch(feed.getUrl());
        } catch (FetchException e) {
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
        Long updateDate = feed.getLastUpdateDate();
        final Map<Long, FeedStat> stats = new LinkedHashMap<Long, FeedStat>();
        for (final Post post : rssFetchResult.getPosts()) {
            if (!isNew(post, feed, refreshDate)) {
                continue;
            }
            
            final long refreshDay = dateHelper.getFirstSecondOfDay(post.getPublishDate());
            if (!stats.containsKey(refreshDay)) {
                FeedStat stat = feedStatDao.find(feed, refreshDay);
                if (stat == null) {
                    stat = FeedStat.builder().feed(feed).refreshDate(refreshDay).count(1).build();
                } else {
                    stat.setCount(stat.getCount() + 1);
                }
                stats.put(refreshDay, stat);
            } else {
                final FeedStat stat = stats.get(refreshDay);
                stat.setCount(stat.getCount() + 1);
            }
            
            if (updateDate == null || updateDate < post.getPublishDate()) {
                updateDate = post.getPublishDate();
            }
        }
        
        feed.setLastUpdateDate(updateDate);
        feed.setLastRefreshDate(refreshDate);
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
    
    boolean isNew(final Post post, final Feed feed, final long refreshDate) {
        if (post.getPublishDate() == null) {
            LOG.warning("Published date is null. Feed: " + feed.getTitle());
            return false;
        }
        if (post.getUri() == null) {
            LOG.warning("URI is null. Feed: " + feed.getTitle());
            return false;
        }
        if (post.getPublishDate() > refreshDate) {
            LOG.warning("Published date " + post.getPublishDate() + " is in the future. Feed: " + feed.getTitle());
            return false;
        }
        if (feed.getLastUpdateDate() != null && post.getPublishDate() <= feed.getLastUpdateDate()) {
            return false;
        }
        return true;
    }
    
    private void updateSubscription(final Feed feed, final long refreshDate, final RssFetchResult rssFetchResult, final Subscription subscription) {
        int counter = 0;
        Long newUpdateDate = subscription.getLastUpdateDate();
        for (final Post post : rssFetchResult.getPosts()) {
            if (!isNew(post, subscription, refreshDate)) {
                continue;
            }
            
            post.setSubscription(subscription);
            if (post.getPublishDate() > refreshDate) {
                post.setPublishDate(refreshDate);
            }
            postDao.save(post);
            ++counter;
            if (newUpdateDate == null || post.getPublishDate() > newUpdateDate) {
                newUpdateDate = post.getPublishDate();
            }
        }
        
        subscription.setLastUpdateDate(newUpdateDate);
        subscriptionDao.save(subscription);
        
        LOG.info("New items (" + subscription.getGroup().getUser().getUsername() + " - " + feed.getUrl() + "): " + counter);
    }
    
    boolean isNew(final Post post, final Subscription subscription, final long refreshDate) {
        if (post.getPublishDate() == null || post.getUri() == null) {
            return false;
        }
        if (post.getPublishDate() > refreshDate) {
            return false;
        }
        if (subscription.getLastUpdateDate() != null && post.getPublishDate() < subscription.getLastUpdateDate()) {
            return false;
        }
        if (postDao.find(subscription, post.getUri(), post.getPublishDate()) != null) {
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
                final Post e = postDao.find(subscription, keptCount);
                if (e != null) {
                    final long threshold = Math.min(date, e.getPublishDate());
                    final List<Post> posts = postDao.listNotBookmarkedAndOlderThan(subscription, threshold);
                    for (final Post post : posts) {
                        postDao.delete(post);
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
        LOG.info("Deleted feed with no subscriptions: " + feed.getUrl());
    }
    
    private void cleanupStats(final Feed feed, final int statsToKeep) {
        final long threshold = dateHelper.substractDaysFromCurrentDate(statsToKeep);
        final List<FeedStat> stats = feedStatDao.listBefore(feed, threshold);
        feedStatDao.deleteAll(stats);
        LOG.info("Deleted stats older than " + new Date(threshold) + " (" + feed.getUrl() + "): " + stats.size());
    }

}
