package jreader.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.User;
import jreader.dto.FeedDto;
import jreader.dto.FeedEntryDto;
import jreader.rss.RssService;
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
	public List<FeedDto> list() {
		List<FeedDto> dtos = new ArrayList<FeedDto>();
		for (Feed feed : feedDao.listAll()) {
			dtos.add(mapper.map(feed, FeedDto.class));
		}
		return dtos;
	}

	@Override
	public List<FeedEntryDto> listEntries(String username, List<Long> feedIds, boolean onlyUnread, boolean ascending) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		
		return convert(user, feedEntryDao.listEntries(user, feedIds, onlyUnread, ascending));
	}
	
	@Override
	public List<FeedEntryDto> listStarredEntries(String username, boolean onlyUnread, boolean ascending) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		
		return convert(user, feedEntryDao.listStarredEntries(user, onlyUnread, ascending));
	}

	private List<FeedEntryDto> convert(User user, List<FeedEntry> starredEntries) {
		List<FeedEntryDto> dtos = new ArrayList<FeedEntryDto>();
		for (FeedEntry starredEntry : starredEntries) {
			FeedEntryDto dto = mapper.map(starredEntry, FeedEntryDto.class);
			dto.setSubscriptionTitle(subscriptionDao.find(user, starredEntry.getFeed()).getTitle());
			dtos.add(dto);
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
			
			List<User> subscribers = subscriptionDao.listSubscribers(feed);
			
			Long lastUpdate = feedDao.getLastUpdatedDate(feed);
			int counter = 0;
			for (jreader.rss.domain.FeedEntry rssFeedEntry : rssFeed.getEntries()) {
				if (lastUpdate != null && lastUpdate < rssFeedEntry.getPublishedDate()) {
					for (User subscriber : subscribers) {
						FeedEntry feedEntry = mapper.map(rssFeedEntry, FeedEntry.class);
						feedEntry.setFeed(feed);
						feedEntry.setUser(subscriber);
						feedEntryDao.save(feedEntry);
					}
					++counter;
				}
			}
			LOG.info(feed.getTitle() + " new items: " + counter);
		}
	}
	
	@Override
	public void cleanup(int olderThanDays, int keptCount) {
		List<Feed> feeds = feedDao.listAll();
		long date = new Date().getTime() - 1000 * 60 * 60 * 24 * olderThanDays;
		for (Feed feed : feeds) {
			for (User user : subscriptionDao.listSubscribers(feed)) {
				int count = 0;
				FeedEntry e = feedEntryDao.find(user, feed, keptCount);
				long threshold = Math.min(date, e.getPublishedDate());
				if (e != null) {
					List<FeedEntry> feedEntries = feedEntryDao.listUnstarredEntriesOlderThan(user, feed, threshold);
					for (FeedEntry feedEntry : feedEntries) {
						feedEntryDao.delete(feedEntry);
						++count;
					}
				}
				LOG.info(feed.getTitle() + "(" + user.getUsername() + ") deleted items older than " + threshold + ": " + count);
			}
		}
	}

}
