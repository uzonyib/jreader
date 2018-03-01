package jreader.dao.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

import jreader.dao.ArchivedPostDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.User;

@Repository
public class ArchivedPostDaoImpl extends AbstractOfyDao<ArchivedPost> implements ArchivedPostDao {

    private static final String ORDER_PROPERTY = "publishDate";

    @Override
    public ArchivedPost find(final Archive archive, final Long id) {
        return getOfy().load().type(ArchivedPost.class).parent(archive).id(id).now();
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
        return getOfy().load().type(ArchivedPost.class).ancestor(ancestor).order(getOrder(page)).offset(page.getOffset()).limit(page.getPageSize()).list();
    }

    private String getOrder(final Pageable page) {
        final Sort sort = page.getSort();
        if (sort == null || sort.getOrderFor(ORDER_PROPERTY) == null) {
            throw new UnsupportedOperationException("Invalid sort order property.");
        }
        final Order order = sort.getOrderFor(ORDER_PROPERTY);
        return order.getDirection().isAscending() ? ORDER_PROPERTY : "-" + ORDER_PROPERTY;
    }

    @Override
    public List<ArchivedPost> list(final Archive archive) {
        return getOfy().load().type(ArchivedPost.class).ancestor(archive).list();
    }

}
