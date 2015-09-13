package jreader.services.impl;

import org.springframework.http.HttpStatus;

import jreader.dao.SubscriptionDao;
import jreader.dao.SubscriptionGroupDao;
import jreader.dao.UserDao;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;
import jreader.services.ServiceException;

abstract class AbstractService {

    protected UserDao userDao;
    protected SubscriptionGroupDao subscriptionGroupDao;
    protected SubscriptionDao subscriptionDao;

    public AbstractService(final UserDao userDao, final SubscriptionGroupDao subscriptionGroupDao, final SubscriptionDao subscriptionDao) {
        this.userDao = userDao;
        this.subscriptionGroupDao = subscriptionGroupDao;
        this.subscriptionDao = subscriptionDao;
    }

    protected User getUser(final String username) {
        final User user = userDao.find(username);
        if (user == null) {
            throw new ServiceException("User not found, username: " + username, HttpStatus.NOT_FOUND);
        }
        return user;
    }

    protected SubscriptionGroup getGroup(final User user, final Long subscriptionGroupId) {
        final SubscriptionGroup group = subscriptionGroupDao.find(user, subscriptionGroupId);
        if (group == null) {
            throw new ServiceException("Group not found, ID: " + subscriptionGroupId, HttpStatus.NOT_FOUND);
        }
        return group;
    }

    protected Subscription getSubscription(final SubscriptionGroup group, final Long subscriptionId) {
        final Subscription subscription = subscriptionDao.find(group, subscriptionId);
        if (subscription == null) {
            throw new ServiceException("Subscription not found, ID: " + subscriptionId, HttpStatus.NOT_FOUND);
        }
        return subscription;
    }

}
