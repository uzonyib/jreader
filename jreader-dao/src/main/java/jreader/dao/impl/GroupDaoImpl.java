package jreader.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.cmd.LoadType;

import jreader.dao.GroupDao;
import jreader.domain.Group;
import jreader.domain.User;

@Repository
public class GroupDaoImpl extends AbstractOfyDao<Group> implements GroupDao {

    @Override
    protected LoadType<Group> getLoadType() {
        return getOfy().load().type(Group.class);
    }

    @Override
    public Optional<Group> find(final User user, final Long id) {
        return Optional.ofNullable(getLoadType().parent(user).id(id).now());
    }

    @Override
    public Optional<Group> find(final User user, final String title) {
        return Optional.ofNullable(getLoadType().ancestor(user).filter("title =", title).first().now());
    }

    @Override
    public List<Group> list(final User user) {
        return getLoadType().ancestor(user).order("order").list();
    }

    @Override
    public int getMaxOrder(final User user) {
        final Group group = getLoadType().ancestor(user).order("-order").first().now();
        return group == null ? -1 : group.getOrder();
    }

}
