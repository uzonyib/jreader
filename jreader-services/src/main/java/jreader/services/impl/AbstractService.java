package jreader.services.impl;

import org.springframework.http.HttpStatus;

import jreader.dao.SubscriptionDao;
import jreader.dao.GroupDao;
import jreader.dao.UserDao;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;
import jreader.services.ServiceException;

abstract class AbstractService {

    protected UserDao userDao;
    protected GroupDao groupDao;
    protected SubscriptionDao subscriptionDao;

    AbstractService(final UserDao userDao, final GroupDao groupDao, final SubscriptionDao subscriptionDao) {
        this.userDao = userDao;
        this.groupDao = groupDao;
        this.subscriptionDao = subscriptionDao;
    }

    protected User getUser(final String username) {
        final User user = userDao.find(username);
        if (user == null) {
            throw new ServiceException("User not found, username: " + username, HttpStatus.NOT_FOUND);
        }
        return user;
    }

    protected Group getGroup(final User user, final Long groupId) {
        final Group group = groupDao.find(user, groupId);
        if (group == null) {
            throw new ServiceException("Group not found, ID: " + groupId, HttpStatus.NOT_FOUND);
        }
        return group;
    }

    protected Subscription getSubscription(final Group group, final Long subscriptionId) {
        final Subscription subscription = subscriptionDao.find(group, subscriptionId);
        if (subscription == null) {
            throw new ServiceException("Subscription not found, ID: " + subscriptionId, HttpStatus.NOT_FOUND);
        }
        return subscription;
    }

}
