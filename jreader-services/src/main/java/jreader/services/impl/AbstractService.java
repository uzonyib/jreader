package jreader.services.impl;

import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.services.ServiceException;
import jreader.services.ServiceStatus;

abstract class AbstractService {
	
	protected UserDao userDao;
	protected SubscriptionGroupDao subscriptionGroupDao;
	protected SubscriptionDao subscriptionDao;
	
	public AbstractService(UserDao userDao, SubscriptionGroupDao subscriptionGroupDao, SubscriptionDao subscriptionDao) {
		this.userDao = userDao;
		this.subscriptionGroupDao = subscriptionGroupDao;
		this.subscriptionDao = subscriptionDao;
	}

	protected User getUser(String username) {
		User user = userDao.find(username);
		if (user == null) {
			throw new ServiceException("User not found.", ServiceStatus.RESOURCE_NOT_FOUND);
		}
		return user;
	}
	
	protected SubscriptionGroup getGroup(User user, Long subscriptionGroupId) {
		SubscriptionGroup group = subscriptionGroupDao.find(user, subscriptionGroupId);
		if (group == null) {
			throw new ServiceException("Group not found, ID " + subscriptionGroupId, ServiceStatus.RESOURCE_NOT_FOUND);
		}
		return group;
	}
	
	protected Subscription getSubscription(SubscriptionGroup group, Long subscriptionId) {
		Subscription subscription = subscriptionDao.find(group, subscriptionId);
		if (subscription == null) {
			throw new ServiceException("Subscription not found, ID: " + subscriptionId, ServiceStatus.RESOURCE_NOT_FOUND);
		}
		return subscription;
	}

}
