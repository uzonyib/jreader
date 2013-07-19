package jreader.dao.impl;

import java.util.List;

import jreader.dao.UserDao;
import jreader.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Work;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;
	
	@Override
	public User find(String username) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(User.class).id(username).now();
	}
	
	@Override
	public List<User> listAll() {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(User.class).list();
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
