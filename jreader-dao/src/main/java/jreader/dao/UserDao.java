package jreader.dao;

import java.util.List;

import jreader.domain.User;

public interface UserDao extends OfyDao<User> {

    User find(String username);
    
    List<User> list(int offset, int count);

}
