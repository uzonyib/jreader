package jreader.services.impl;

import org.springframework.http.HttpStatus;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.services.ServiceException;
import jreader.services.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
    
    public UserServiceImpl(final UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Override
    public boolean isRegistered(final String username) {
        return userDao.find(username) != null;
    }

    @Override
    public void register(final String username, final Role role) {
        if (userDao.find(username) != null) {
            throw new ServiceException("User already exists.", HttpStatus.CONFLICT);
        }
        final User user = new User();
        user.setUsername(username);
        user.setRole(role);
        userDao.save(user);
    }
    
    @Override
    public Role getRole(final String username) {
        final User user = userDao.find(username);
        if (user == null) {
            return null;
        }
        return user.getRole();
    }

}
