package jreader.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import jreader.dao.GroupDao;
import jreader.domain.Group;
import jreader.domain.User;

@Repository
public class GroupDaoImpl extends AbstractOfyDao<Group> implements GroupDao {

    @Override
    public Group find(final User user, final Long id) {
        return getOfy().load().type(Group.class).parent(user).id(id).now();
    }

    @Override
    public Group find(final User user, final String title) {
        return getOfy().load().type(Group.class).ancestor(user).filter("title =", title).first().now();
    }

    @Override
    public List<Group> list(final User user) {
        return getOfy().load().type(Group.class).ancestor(user).order("order").list();
    }

    @Override
    public int getMaxOrder(final User user) {
        final Group group = getOfy().load().type(Group.class).ancestor(user)
                .order("-order").first().now();
        return group == null ? -1 : group.getOrder();
    }

}
