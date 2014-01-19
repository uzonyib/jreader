package jreader.dao.impl;

import jreader.dao.UserDao;
import jreader.domain.User;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

public class UserDaoImpl extends AbstractOfyDao<User> implements UserDao {
	
	public UserDaoImpl(ObjectifyFactory objectifyFactory) {
		super(objectifyFactory);
	}

	@Override
	public User find(String username) {
		Objectify ofy = getOfy();
		return ofy.load().type(User.class).id(username).now();
	}

}
