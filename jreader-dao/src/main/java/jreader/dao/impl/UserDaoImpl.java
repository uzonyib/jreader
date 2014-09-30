package jreader.dao.impl;

import jreader.dao.UserDao;
import jreader.domain.User;

public class UserDaoImpl extends AbstractOfyDao<User> implements UserDao {

    @Override
    public User find(final String username) {
        return getOfy().load().type(User.class).id(username).now();
    }

}
