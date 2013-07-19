package jreader.dao;

import java.util.List;

import jreader.domain.User;

public interface UserDao {
	
	User find(String username);
	
	List<User> listAll();
	
	User save(User user);

}
