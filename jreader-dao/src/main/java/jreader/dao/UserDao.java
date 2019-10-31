package jreader.dao;

import java.util.List;
import java.util.Optional;

import jreader.domain.User;

public interface UserDao extends OfyDao<User> {

    Optional<User> find(String username);
    
    List<User> list(int offset, int count);

}
