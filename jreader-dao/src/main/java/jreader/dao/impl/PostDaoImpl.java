package jreader.dao.impl;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import jreader.dao.PostDao;
import jreader.dao.PostFilter;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.User;

@Repository
public class PostDaoImpl extends AbstractOfyDao<Post> implements PostDao {

    private static final String ORDER_PROPERTY = "publishDate";

    @Override
    protected LoadType<Post> getLoadType() {
        return getOfy().load().type(Post.class);
    }

    @Override
    public Optional<Post> find(final Subscription subscription, final Long id) {
        return Optional.ofNullable(getLoadType().parent(subscription).id(id).now());
    }

    @Override
    public Optional<Post> find(final Subscription subscription, final String uri, final long publishDate) {
        return Optional.ofNullable(getLoadType().ancestor(subscription).filter("uri =", uri).filter("publishDate =", publishDate).first().now());
    }

    @Override
    public Optional<Post> find(final Subscription subscription, final int ordinal) {
        return Optional.ofNullable(getLoadType().ancestor(subscription).order("-publishDate").offset(ordinal - 1).limit(1).first().now());
    }

    @Override
    public List<Post> list(final User user, final PostFilter filter) {
        return listForAncestor(user, filter);
    }

    @Override
    public List<Post> list(final Group group, final PostFilter filter) {
        return listForAncestor(group, filter);
    }

    @Override
    public List<Post> list(final Subscription subscription, final PostFilter filter) {
        return listForAncestor(subscription, filter);
    }

    private List<Post> listForAncestor(final Object ancestor, final PostFilter filter) {
        Query<Post> query = getLoadType().ancestor(ancestor);
        switch (filter.getPostType()) {
            case UNREAD:
                query = query.filter("read", false);
                break;
            case BOOKMARKED:
                query = query.filter("bookmarked", true);
                break;
            default:
                break;
        }
        return query.order(getOrder(filter)).offset(filter.getPage().getOffset()).limit(filter.getPage().getPageSize()).list();
    }

    private String getOrder(final PostFilter filter) {
        final Sort sort = filter.getPage().getSort();
        if (isNull(sort) || isNull(sort.getOrderFor(ORDER_PROPERTY))) {
            throw new UnsupportedOperationException("Invalid sort order property.");
        }
        return sort.getOrderFor(ORDER_PROPERTY).getDirection().isAscending() ? ORDER_PROPERTY : "-" + ORDER_PROPERTY;
    }

    @Override
    public List<Post> listNotBookmarkedAndOlderThan(final Subscription subscription, final long date) {
        return getLoadType().ancestor(subscription).filter("bookmarked", false).filter("publishDate <", date).list();
    }

    @Override
    public List<Post> list(final Subscription subscription) {
        return getLoadType().ancestor(subscription).list();
    }

    @Override
    public int countUnread(final Subscription subscription) {
        return getLoadType().ancestor(subscription).filter("read", false).count();
    }

}
