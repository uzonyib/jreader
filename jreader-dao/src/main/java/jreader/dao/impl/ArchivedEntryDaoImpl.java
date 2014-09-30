package jreader.dao.impl;

import java.util.List;

import jreader.dao.ArchivedEntryDao;
import jreader.dao.ArchivedEntryFilter;
import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.User;

public class ArchivedEntryDaoImpl extends AbstractOfyDao<ArchivedEntry> implements ArchivedEntryDao {

    @Override
    public ArchivedEntry find(final Archive archive, final Long id) {
        return getOfy().load().type(ArchivedEntry.class).parent(archive).id(id).now();
    }

    @Override
    public List<ArchivedEntry> list(final User user, final ArchivedEntryFilter filter) {
        return listForAncestor(user, filter);
    }

    @Override
    public List<ArchivedEntry> list(final Archive archive, final ArchivedEntryFilter filter) {
        return listForAncestor(archive, filter);
    }

    private List<ArchivedEntry> listForAncestor(final Object ancestor, final ArchivedEntryFilter filter) {
        return getOfy().load().type(ArchivedEntry.class).ancestor(ancestor)
                .order(filter.isAscending() ? "publishedDate" : "-publishedDate")
                .offset(filter.getOffset()).limit(filter.getCount()).list();
    }

}
