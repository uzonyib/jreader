package jreader.service.impl;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import jreader.dao.UserDao;
import jreader.domain.User;
import jreader.dto.UserDto;
import jreader.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	@Qualifier("servicesMapper")
	private Mapper mapper;

	@Override
	public UserDto getUser(String username) {
		return mapper.map(userDao.find(username), UserDto.class);
	}

	@Override
	public void register(String username) {
		if (userDao.find(username) == null) {
			User user = new User();
			user.setUsername(username);
			userDao.save(user);
		}
	}

}
