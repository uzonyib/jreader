package jreader.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.cmd.LoadType;

import jreader.dao.UserDao;
import jreader.domain.User;

@Repository
public class UserDaoImpl extends AbstractOfyDao<User> implements UserDao {

    @Override
    protected LoadType<User> getLoadType() {
        return getOfy().load().type(User.class);
    }

    @Override
    public Optional<User> find(final String username) {
        return Optional.ofNullable(getLoadType().id(username).now());
    }

    @Override
    public List<User> list(final int offset, final int count) {
        return getLoadType().offset(offset).limit(count).orderKey(false).list();
    }

}
