package jreader.service.impl;

import jreader.dao.UserDao;
import jreader.domain.User;
import jreader.service.UserService;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao;

	@Override
	public void register(String username) {
		if (userDao.find(username) == null) {
			User user = new User();
			user.setUsername(username);
			userDao.save(user);
		}
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
