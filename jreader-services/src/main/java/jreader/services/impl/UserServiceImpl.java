package jreader.services.impl;

import org.springframework.http.HttpStatus;

import jreader.dao.UserDao;
import jreader.domain.User;
import jreader.services.ServiceException;
import jreader.services.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void register(final String username) {
        if (userDao.find(username) != null) {
            throw new ServiceException("User already exists.", HttpStatus.CONFLICT);
        }
        final User user = new User();
        user.setUsername(username);
        userDao.save(user);
    }
    
    @Override
    public void ensureIsRegistered(final String username) {
        if (userDao.find(username) == null) {
            this.register(username);
        }
    }

}
