package jreader.dao.impl;

import jreader.dao.UserDao;
import jreader.domain.User;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Work;

public class UserDaoImpl implements UserDao {
	
	private ObjectifyFactory objectifyFactory;
	
	public UserDaoImpl(ObjectifyFactory objectifyFactory) {
		this.objectifyFactory = objectifyFactory;
	}

	@Override
	public User find(String username) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(User.class).id(username).now();
	}

	@Override
	public User save(final User user) {
		final Objectify ofy = objectifyFactory.begin();
		return ofy.transact(new Work<User>() {
			@Override
			public User run() {
				Key<User> key = ofy.save().entity(user).now();
				return ofy.load().key(key).now();
			}
		});
	}

}
