package jreader.services.impl;

import org.springframework.util.Assert;

import jreader.dao.GroupDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Group;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.services.exception.ResourceNotFoundException;

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
        Assert.hasLength(username, "Username invalid.");

        final User user = userDao.find(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found, username: " + username);
        }
        return user;
    }

    protected Group getGroup(final User user, final Long groupId) {
        final Group group = groupDao.find(user, groupId);
        if (group == null) {
            throw new ResourceNotFoundException("Group not found, ID: " + groupId);
        }
        return group;
    }

    protected Subscription getSubscription(final Group group, final Long subscriptionId) {
        final Subscription subscription = subscriptionDao.find(group, subscriptionId);
        if (subscription == null) {
            throw new ResourceNotFoundException("Subscription not found, ID: " + subscriptionId);
        }
        return subscription;
    }

}
