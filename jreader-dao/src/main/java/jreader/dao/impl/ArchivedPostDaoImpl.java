package jreader.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import jreader.dao.ArchivedPostDao;
import jreader.dao.ArchivedPostFilter;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.User;

@Repository
public class ArchivedPostDaoImpl extends AbstractOfyDao<ArchivedPost> implements ArchivedPostDao {

    @Override
    public ArchivedPost find(final Archive archive, final Long id) {
        return getOfy().load().type(ArchivedPost.class).parent(archive).id(id).now();
    }

    @Override
    public List<ArchivedPost> list(final User user, final ArchivedPostFilter filter) {
        return listForAncestor(user, filter);
    }

    @Override
    public List<ArchivedPost> list(final Archive archive, final ArchivedPostFilter filter) {
        return listForAncestor(archive, filter);
    }

    private List<ArchivedPost> listForAncestor(final Object ancestor, final ArchivedPostFilter filter) {
        return getOfy().load().type(ArchivedPost.class).ancestor(ancestor)
                .order(filter.isAscending() ? "publishDate" : "-publishDate")
                .offset(filter.getOffset()).limit(filter.getCount()).list();
    }
    
    @Override
    public List<ArchivedPost> list(final Archive archive) {
        return getOfy().load().type(ArchivedPost.class).ancestor(archive).list();
    }

}
