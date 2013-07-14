package jreader.dao.impl;

import jreader.dao.UserDao;
import jreader.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;

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
	public void save(final User user) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entity(user).now();
			}
		});
	}

}
