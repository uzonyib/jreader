package jreader.dao;

import java.util.List;

import jreader.domain.Group;
import jreader.domain.User;

public interface GroupDao extends OfyDao<Group> {

    Group find(User user, Long id);

    Group find(User user, String title);

    List<Group> list(User user);

    int getMaxOrder(User user);

}
