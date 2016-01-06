package jreader.dao.impl;

import java.util.Collection;

import jreader.dao.OfyDao;
import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.FeedStat;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

abstract class AbstractOfyDao<EntityType> implements OfyDao<EntityType> {

    static {
        final ObjectifyFactory factory = factory();
        factory.register(User.class);
        factory.register(Group.class);
        factory.register(Subscription.class);
        factory.register(Feed.class);
        factory.register(FeedEntry.class);
        factory.register(FeedStat.class);
        factory.register(Archive.class);
        factory.register(ArchivedEntry.class);
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
        return ofy.transact(new Work<EntityType>() {
            @Override
            public EntityType run() {
                final Key<EntityType> key = ofy.save().entity(entity).now();
                return ofy.load().key(key).now();
            }
        });
    }

    @Override
    public void saveAll(final Collection<EntityType> entities) {
        final Objectify ofy = getOfy();
        ofy.transact(new VoidWork() {
            @Override
            public void vrun() {
                ofy.save().entities(entities).now();
            }
        });
    }

    @Override
    public void delete(final EntityType entity) {
        final Objectify ofy = getOfy();
        ofy.transact(new VoidWork() {
            @Override
            public void vrun() {
                ofy.delete().entity(entity).now();
            }
        });
    }

    @Override
    public void deleteAll(final Collection<EntityType> entities) {
        final Objectify ofy = getOfy();
        ofy.transact(new VoidWork() {
            @Override
            public void vrun() {
                ofy.delete().entities(entities).now();
            }
        });
    }

}
