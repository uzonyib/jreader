package jreader.services.impl;

import org.springframework.http.HttpStatus;

import jreader.dao.UserDao;
import jreader.domain.Role;
import jreader.domain.User;
import jreader.services.ServiceException;
import jreader.services.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;
    
    private com.google.appengine.api.users.UserService googleUserService;

    public UserServiceImpl(final UserDao userDao, final com.google.appengine.api.users.UserService googleUserService) {
        this.userDao = userDao;
        this.googleUserService = googleUserService;
    }

    @Override
    public void register(final String username) {
        if (userDao.find(username) != null) {
            throw new ServiceException("User already exists.", HttpStatus.CONFLICT);
        }
        final User user = new User();
        user.setUsername(username);
        user.setRole(googleUserService.isUserAdmin() ? Role.ADMIN : Role.UNAUTHORIZED);
        userDao.save(user);
    }
    
    @Override
    public void ensureIsRegistered(final String username) {
        if (userDao.find(username) == null) {
            this.register(username);
        }
    }

}
