package jreader.dao.impl;

import java.util.List;

import jreader.dao.FeedEntryDao;
import jreader.dao.FeedEntryFilter;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import com.googlecode.objectify.cmd.Query;

public class FeedEntryDaoImpl extends AbstractOfyDao<FeedEntry> implements FeedEntryDao {

    @Override
    public FeedEntry find(final Subscription subscription, final Long id) {
        return getOfy().load().type(FeedEntry.class).parent(subscription).id(id).now();
    }
    
    @Override
    public FeedEntry find(final Subscription subscription, final String uri, final long publishDate) {
        return getOfy().load().type(FeedEntry.class).ancestor(subscription).filter("uri =", uri).filter("publishDate =", publishDate).first().now();
    }

    @Override
    public FeedEntry find(final Subscription subscription, final int ordinal) {
        return getOfy().load().type(FeedEntry.class).ancestor(subscription).order("-publishDate")
                .offset(ordinal - 1).limit(1).first().now();
    }

    @Override
    public List<FeedEntry> list(final User user, final FeedEntryFilter filter) {
        return listForAncestor(user, filter);
    }

    @Override
    public List<FeedEntry> list(final SubscriptionGroup subscriptionGroup, final FeedEntryFilter filter) {
        return listForAncestor(subscriptionGroup, filter);
    }

    @Override
    public List<FeedEntry> list(final Subscription subscription, final FeedEntryFilter filter) {
        return listForAncestor(subscription, filter);
    }

    private List<FeedEntry> listForAncestor(final Object ancestor, final FeedEntryFilter filter) {
        Query<FeedEntry> query = getOfy().load().type(FeedEntry.class).ancestor(ancestor);
        switch (filter.getSelection()) {
            case UNREAD:
                query = query.filter("read", false);
                break;
            case STARRED:
                query = query.filter("starred", true);
                break;
            default:
                break;
        }
        return query.order(filter.isAscending() ? "publishDate" : "-publishDate")
                .offset(filter.getOffset()).limit(filter.getCount()).list();
    }

    @Override
    public List<FeedEntry> listUnstarredOlderThan(final Subscription subscription, final long date) {
        return getOfy().load().type(FeedEntry.class).ancestor(subscription).filter("starred", false)
                .filter("publishDate <", date).list();
    }

    @Override
    public List<FeedEntry> list(final Subscription subscription) {
        return getOfy().load().type(FeedEntry.class).ancestor(subscription).list();
    }

    @Override
    public int countUnread(final Subscription subscription) {
        return getOfy().load().type(FeedEntry.class).ancestor(subscription).filter("read", false).count();
    }

}
