package jreader.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.dao.FeedEntryFilter;
import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.dto.FeedEntryDto;
import jreader.services.FeedEntryFilterData;
import jreader.services.FeedEntryService;

import org.springframework.core.convert.ConversionService;

public class FeedEntryServiceImpl implements FeedEntryService {
	
	private UserDao userDao;
	private SubscriptionGroupDao subscriptionGroupDao;
	private SubscriptionDao subscriptionDao;
	private FeedEntryDao feedEntryDao;
	
	private ConversionService conversionService;

	@Override
	public void markRead(String username, List<Long> subscriptionGroupIds, List<Long> subscriptionIds, List<Long> feedEntryIds) {
		if (feedEntryIds == null || subscriptionIds == null || subscriptionGroupIds == null || feedEntryIds.size() != subscriptionIds.size() || feedEntryIds.size() != subscriptionGroupIds.size()) {
			return;
		}
		
		User user = userDao.find(username);
		if (user == null) {
			return;
		}

		List<FeedEntry> feedEntriesToSave = new ArrayList<FeedEntry>();
		for (int i = 0; i < feedEntryIds.size(); ++i) {
			SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupIds.get(i));
			if (subscriptionGroup == null) {
				continue;
			}
			Subscription subscription = subscriptionDao.find(subscriptionGroup, subscriptionIds.get(i));
			if (subscription == null) {
				continue;
			}
			FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryIds.get(i));
			if (feedEntry != null && !feedEntry.isRead()) {
				feedEntry.setRead(true);
				feedEntriesToSave.add(feedEntry);
			}
		}
		feedEntryDao.saveAll(feedEntriesToSave);
	}
	
	@Override
	public void star(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		Subscription subscription = subscriptionDao.find(subscriptionGroup, subscriptionId);
		if (subscription == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
		if (feedEntry != null && !feedEntry.isStarred()) {
			feedEntry.setStarred(true);
			feedEntryDao.save(feedEntry);
		}
	}
	
	@Override
	public void unstar(String username, Long subscriptionGroupId, Long subscriptionId, Long feedEntryId) {
		User user = userDao.find(username);
		if (user == null) {
			return;
		}
		
		SubscriptionGroup subscriptionGroup = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (subscriptionGroup == null) {
			return;
		}
		Subscription subscription = subscriptionDao.find(subscriptionGroup, subscriptionId);
		if (subscription == null) {
			return;
		}
		
		FeedEntry feedEntry = feedEntryDao.find(subscription, feedEntryId);
		if (feedEntry != null && feedEntry.isStarred()) {
			feedEntry.setStarred(false);
			feedEntryDao.save(feedEntry);
		}
	}
	
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

	private List<FeedEntryDto> convert(User user, List<FeedEntry> feedEntries) {
		List<FeedEntryDto> dtos = new ArrayList<FeedEntryDto>();
		for (FeedEntry feedEntry : feedEntries) {
			dtos.add(conversionService.convert(feedEntry, FeedEntryDto.class));
		}
		return dtos;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public SubscriptionGroupDao getSubscriptionGroupDao() {
		return subscriptionGroupDao;
	}

	public void setSubscriptionGroupDao(SubscriptionGroupDao subscriptionGroupDao) {
		this.subscriptionGroupDao = subscriptionGroupDao;
	}

	public SubscriptionDao getSubscriptionDao() {
		return subscriptionDao;
	}

	public void setSubscriptionDao(SubscriptionDao subscriptionDao) {
		this.subscriptionDao = subscriptionDao;
	}

	public FeedEntryDao getFeedEntryDao() {
		return feedEntryDao;
	}

	public void setFeedEntryDao(FeedEntryDao feedEntryDao) {
		this.feedEntryDao = feedEntryDao;
	}

	public ConversionService getConversionService() {
		return conversionService;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

}
