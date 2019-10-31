package jreader.dao;

import java.util.List;
import java.util.Optional;

import jreader.domain.Group;
import jreader.domain.User;

public interface GroupDao extends OfyDao<Group> {

    Optional<Group> find(User user, Long id);

    Optional<Group> find(User user, String title);

    List<Group> list(User user);

    int getMaxOrder(User user);

}
