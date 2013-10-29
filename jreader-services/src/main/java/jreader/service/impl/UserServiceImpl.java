package jreader.service.impl;

import jreader.dao.UserDao;
import jreader.domain.User;
import jreader.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	@Override
	public void register(String username) {
		if (userDao.find(username) == null) {
			User user = new User();
			user.setUsername(username);
			userDao.save(user);
		}
	}

}
