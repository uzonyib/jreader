package jreader.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import jreader.dao.ArchiveDao;
import jreader.domain.Archive;
import jreader.domain.User;

@Repository
public class ArchiveDaoImpl extends AbstractOfyDao<Archive> implements ArchiveDao {

    @Override
    public Archive find(final User user, final Long id) {
        return getOfy().load().type(Archive.class).parent(user).id(id).now();
    }

    @Override
    public Archive find(final User user, final String title) {
        return getOfy().load().type(Archive.class).ancestor(user).filter("title =", title).first().now();
    }

    @Override
    public List<Archive> list(final User user) {
        return getOfy().load().type(Archive.class).ancestor(user).order("order").list();
    }

    @Override
    public int getMaxOrder(final User user) {
        final Archive archive = getOfy().load().type(Archive.class).ancestor(user).order("-order").first().now();
        return archive == null ? -1 : archive.getOrder();
    }

}
