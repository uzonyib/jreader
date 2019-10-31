package jreader.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.services.DateHelper;
import jreader.services.UserService;
import jreader.services.exception.ResourceAlreadyExistsException;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private DateHelper dateHelper;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final DateHelper dateHelper) {
        this.userDao = userDao;
        this.dateHelper = dateHelper;
    }

    @Override
    public boolean isRegistered(final String username) {
        return userDao.find(username).isPresent();
    }

    @Override
    public void register(final String username, final Role role) {
        Assert.hasLength(username, "Invalid username.");
        Assert.notNull(role, "Invalid role.");
        if (userDao.find(username).isPresent()) {
            throw new ResourceAlreadyExistsException("User with username '" + username + "' already exists.");
        }

        final User user = new User();
        user.setUsername(username);
        user.setRole(role);
        user.setRegistrationDate(dateHelper.getCurrentDate());
        userDao.save(user);
    }

    @Override
    public Role getRole(final String username) {
        Assert.hasLength(username, "Invalid username.");

        return userDao.find(username).map(User::getRole).orElse(null);
    }

}
