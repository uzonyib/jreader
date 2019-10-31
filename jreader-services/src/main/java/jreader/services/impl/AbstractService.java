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
        return userDao.find(username).orElseThrow(() -> new ResourceNotFoundException("User not found, username: " + username));
    }

    protected Group getGroup(final User user, final Long groupId) {
        return groupDao.find(user, groupId).orElseThrow(() -> new ResourceNotFoundException("Group not found, ID: " + groupId));
    }

    protected Subscription getSubscription(final Group group, final Long subscriptionId) {
        return subscriptionDao.find(group, subscriptionId).orElseThrow(() -> new ResourceNotFoundException("Subscription not found, ID: " + subscriptionId));
    }

}
