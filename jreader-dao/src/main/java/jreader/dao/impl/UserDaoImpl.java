package jreader.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import jreader.dao.UserDao;
import jreader.domain.User;

@Repository
public class UserDaoImpl extends AbstractOfyDao<User> implements UserDao {

    @Override
    public User find(final String username) {
        return getOfy().load().type(User.class).id(username).now();
    }
    
    @Override
    public List<User> list(final int offset, final int count) {
        return getOfy().load().type(User.class).offset(offset).limit(count).orderKey(false).list();
    }

}
