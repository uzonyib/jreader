package jreader.dao.impl;

import java.util.List;

import jreader.dao.PostDao;
import jreader.dao.PostFilter;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

import com.googlecode.objectify.cmd.Query;

public class PostDaoImpl extends AbstractOfyDao<Post> implements PostDao {

    @Override
    public Post find(final Subscription subscription, final Long id) {
        return getOfy().load().type(Post.class).parent(subscription).id(id).now();
    }
    
    @Override
    public Post find(final Subscription subscription, final String uri, final long publishDate) {
        return getOfy().load().type(Post.class).ancestor(subscription).filter("uri =", uri).filter("publishDate =", publishDate).first().now();
    }

    @Override
    public Post find(final Subscription subscription, final int ordinal) {
        return getOfy().load().type(Post.class).ancestor(subscription).order("-publishDate")
                .offset(ordinal - 1).limit(1).first().now();
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
        Query<Post> query = getOfy().load().type(Post.class).ancestor(ancestor);
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
        return query.order(filter.isAscending() ? "publishDate" : "-publishDate")
                .offset(filter.getOffset()).limit(filter.getCount()).list();
    }

    @Override
    public List<Post> listNotBookmarkedAndOlderThan(final Subscription subscription, final long date) {
        return getOfy().load().type(Post.class).ancestor(subscription).filter("bookmarked", false)
                .filter("publishDate <", date).list();
    }

    @Override
    public List<Post> list(final Subscription subscription) {
        return getOfy().load().type(Post.class).ancestor(subscription).list();
    }

    @Override
    public int countUnread(final Subscription subscription) {
        return getOfy().load().type(Post.class).ancestor(subscription).filter("read", false).count();
    }

}
