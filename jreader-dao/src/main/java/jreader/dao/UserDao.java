package jreader.dao;

import jreader.domain.User;

public interface UserDao {
	
	User find(String username);
	
	void save(User user);

}
