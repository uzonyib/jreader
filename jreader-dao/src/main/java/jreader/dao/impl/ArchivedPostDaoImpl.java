package jreader.dao.impl;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.cmd.LoadType;

import jreader.dao.ArchivedPostDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.User;

@Repository
public class ArchivedPostDaoImpl extends AbstractOfyDao<ArchivedPost> implements ArchivedPostDao {

    private static final String ORDER_PROPERTY = "publishDate";

    @Override
    protected LoadType<ArchivedPost> getLoadType() {
        return getOfy().load().type(ArchivedPost.class);
    }

    @Override
    public Optional<ArchivedPost> find(final Archive archive, final Long id) {
        return Optional.ofNullable(getLoadType().parent(archive).id(id).now());
    }

    @Override
    public List<ArchivedPost> list(final User user, final Pageable page) {
        return listForAncestor(user, page);
    }

    @Override
    public List<ArchivedPost> list(final Archive archive, final Pageable page) {
        return listForAncestor(archive, page);
    }

    private List<ArchivedPost> listForAncestor(final Object ancestor, final Pageable page) {
        return getLoadType().ancestor(ancestor).order(getOrder(page)).offset(page.getOffset()).limit(page.getPageSize()).list();
    }

    private String getOrder(final Pageable page) {
        final Sort sort = page.getSort();
        if (isNull(sort) || isNull(sort.getOrderFor(ORDER_PROPERTY))) {
            throw new UnsupportedOperationException("Invalid sort order property.");
        }
        return sort.getOrderFor(ORDER_PROPERTY).getDirection().isAscending() ? ORDER_PROPERTY : "-" + ORDER_PROPERTY;
    }

    @Override
    public List<ArchivedPost> list(final Archive archive) {
        return getLoadType().ancestor(archive).list();
    }

}
