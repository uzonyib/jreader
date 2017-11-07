package jreader.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jreader.dao.DaoFacade;
import jreader.dao.GroupDao;
import jreader.dao.SubscriptionDao;
import jreader.dao.UserDao;
import jreader.domain.Group;
import jreader.domain.Role;
import jreader.domain.Subscription;
import jreader.domain.User;
import jreader.services.exception.ResourceNotFoundException;

public class AbstractServiceTest extends ServiceTest {

    private SubscriptionServiceImpl sut;

    @Mock
    private UserDao userDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private SubscriptionDao subscriptionDao;

    private User user;
    private Group group;
    private Subscription subscription;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);

        user = new User("user", Role.USER, 1L);
        group = Group.builder().user(user).id(0L).title("group").order(1).build();
        subscription = Subscription.builder().group(group).id(0L).title("subscription 0").order(1).build();

        final DaoFacade daoFacade = DaoFacade.builder().userDao(userDao).groupDao(groupDao).subscriptionDao(subscriptionDao).build();
        sut = new SubscriptionServiceImpl(daoFacade, null, null);
    }

    @Test
    public void getUserShouldReturnUserIfUserExists() {
        when(userDao.find(user.getUsername())).thenReturn(user);

        final User actual = sut.getUser(user.getUsername());

        assertThat(actual).isEqualTo(user);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void getUserShouldThrowExceptionIfUserNotExists() {
        when(userDao.find(user.getUsername())).thenReturn(null);

        sut.getUser(user.getUsername());
    }

    @Test
    public void getGroupShouldReturnGroupIfGroupExists() {
        when(groupDao.find(user, group.getId())).thenReturn(group);

        final Group actual = sut.getGroup(user, group.getId());

        assertThat(actual).isEqualTo(group);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void getGroupShouldThrowExceptionIfGroupNotExists() {
        when(groupDao.find(user, group.getId())).thenReturn(null);

        sut.getGroup(user, group.getId());
    }

    @Test
    public void getSubscriptionShouldReturnSubscriptionIfSubscriptionExists() {
        when(subscriptionDao.find(group, subscription.getId())).thenReturn(subscription);

        final Subscription actual = sut.getSubscription(group, subscription.getId());

        assertThat(actual).isEqualTo(subscription);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void getSubscriptionShouldThrowExceptionIfSubscriptionNotExists() {
        when(subscriptionDao.find(group, subscription.getId())).thenReturn(null);

        sut.getSubscription(group, subscription.getId());
    }

}
