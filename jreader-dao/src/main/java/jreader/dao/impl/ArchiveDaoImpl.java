package jreader.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.googlecode.objectify.cmd.LoadType;

import jreader.dao.ArchiveDao;
import jreader.domain.Archive;
import jreader.domain.User;

@Repository
public class ArchiveDaoImpl extends AbstractOfyDao<Archive> implements ArchiveDao {

    @Override
    protected LoadType<Archive> getLoadType() {
        return getOfy().load().type(Archive.class);
    }

    @Override
    public Optional<Archive> find(final User user, final Long id) {
        return Optional.ofNullable(getLoadType().parent(user).id(id).now());
    }

    @Override
    public Optional<Archive> find(final User user, final String title) {
        return Optional.ofNullable(getLoadType().ancestor(user).filter("title =", title).first().now());
    }

    @Override
    public List<Archive> list(final User user) {
        return getLoadType().ancestor(user).order("order").list();
    }

    @Override
    public int getMaxOrder(final User user) {
        final Archive archive = getLoadType().ancestor(user).order("-order").first().now();
        return archive == null ? -1 : archive.getOrder();
    }

}
