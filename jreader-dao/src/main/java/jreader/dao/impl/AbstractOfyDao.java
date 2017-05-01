package jreader.dao.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import jreader.dao.OfyDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedPost;
import jreader.domain.Feed;
import jreader.domain.FeedStat;
import jreader.domain.Group;
import jreader.domain.Post;
import jreader.domain.Subscription;
import jreader.domain.User;

abstract class AbstractOfyDao<EntityType> implements OfyDao<EntityType> {

    private static final List<Class<?>> DOMAIN_CLASSES = Arrays.asList(
            User.class, Group.class, Subscription.class, Feed.class, Post.class, FeedStat.class, Archive.class, ArchivedPost.class);

    static {
        final ObjectifyFactory factory = factory();
        DOMAIN_CLASSES.forEach(clazz -> factory.register(clazz));
    }

    private static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

    protected static Objectify getOfy() {
        return factory().begin();
    }

    @Override
    public EntityType save(final EntityType entity) {
        final Objectify ofy = getOfy();
        return ofy.transact(() -> {
            final Key<EntityType> key = ofy.save().entity(entity).now();
            return ofy.load().key(key).now();
        });
    }

    @Override
    public void saveAll(final Collection<EntityType> entities) {
        final Objectify ofy = getOfy();
        ofy.transact(() -> ofy.save().entities(entities).now());
    }

    @Override
    public void delete(final EntityType entity) {
        final Objectify ofy = getOfy();
        ofy.transact(() -> ofy.delete().entity(entity).now());
    }

    @Override
    public void deleteAll(final Collection<EntityType> entities) {
        final Objectify ofy = getOfy();
        ofy.transact(() -> ofy.delete().entities(entities).now());
    }

}
