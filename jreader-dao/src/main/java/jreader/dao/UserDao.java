package jreader.dao;

import jreader.domain.User;

public interface UserDao extends OfyDao<User> {
	
	User find(String username);

}
