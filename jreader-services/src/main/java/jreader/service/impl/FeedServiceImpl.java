package jreader.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import jreader.dao.ActionDao;
import jreader.dao.FeedDao;
import jreader.dao.FeedEntryDao;
import jreader.dao.UserDao;
import jreader.domain.Action;
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
	private FeedDao feedDao;

	@Autowired
	private FeedEntryDao feedEntryDao;
	
	@Autowired
	private ActionDao actionDao;
	
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
	public List<FeedEntryDto> listEntries(String username, String feedId) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		
		Feed feed = feedDao.find(feedId);
		if (feed == null) {
			return Collections.emptyList();
		}
		List<FeedEntry> entries = feedEntryDao.listEntries(feed);
		List<FeedEntryDto> dtos = new ArrayList<FeedEntryDto>();
		for (FeedEntry feedEntry : entries) {
			FeedEntryDto dto = mapper.map(feedEntry, FeedEntryDto.class);
			dto.setRead(actionDao.isRead(user, feedEntry));
			dto.setStarred(actionDao.isStarred(user, feedEntry));
			dtos.add(dto);
		}
		return dtos;
	}
	
	@Override
	public List<FeedEntryDto> listStarredEntries(String username) {
		User user = userDao.find(username);
		if (user == null) {
			return Collections.emptyList();
		}
		
		List<Action> starActions = actionDao.list(user, ActionDao.STAR_ACTION_TYPE);
		List<FeedEntryDto> dtos = new ArrayList<FeedEntryDto>();
		for (Action starAction : starActions) {
			FeedEntry feedEntry = starAction.getFeedEntry();
			FeedEntryDto dto = mapper.map(feedEntry, FeedEntryDto.class);
			dto.setRead(actionDao.isRead(user, feedEntry));
			dto.setStarred(true);
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
			int counter = 0;
			for (jreader.rss.domain.FeedEntry rssFeedEntry : rssFeed.getEntries()) {
				FeedEntry feedEntry = mapper.map(rssFeedEntry, FeedEntry.class);
				if (feedEntryDao.findByLink(feed, feedEntry.getLink()) == null) {
					feedEntry.setFeed(feed);
					feedEntryDao.save(feedEntry);
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
			FeedEntry e = feedEntryDao.find(feed, keptCount);
			if (e == null) {
				continue;
			}
			
			int count = 0;
			long threshold = Math.min(date, e.getPublishedDate());
			List<FeedEntry> feedEntries = feedEntryDao.listEntriesOlderThan(feed, threshold);
			for (FeedEntry feedEntry : feedEntries) {
				if (!actionDao.isStarred(feedEntry)) {
					feedEntryDao.delete(feedEntry);
					++count;
				}
			}
			LOG.info(feed.getTitle() + " deleted items older than " + threshold + ": " + count);
		}
	}

}
