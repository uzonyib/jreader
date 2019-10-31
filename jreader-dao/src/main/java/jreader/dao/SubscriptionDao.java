package jreader.dao;

import java.util.List;
import java.util.Optional;

import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.Group;
import jreader.domain.User;

public interface SubscriptionDao extends OfyDao<Subscription> {

    Optional<Subscription> find(User user, Feed feed);

    Optional<Subscription> find(Group group, Long id);
    
    Optional<Subscription> find(Group group, String title);

    List<Subscription> listSubscriptions(Feed feed);

    List<Subscription> list(Group group);

    int countSubscribers(Feed feed);

    int getMaxOrder(Group group);

}
