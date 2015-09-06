package jreader.dao;

import java.util.List;

import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface SubscriptionDao extends OfyDao<Subscription> {

    Subscription find(User user, Feed feed);

    Subscription find(SubscriptionGroup subscriptionGroup, Long id);
    
    Subscription find(SubscriptionGroup subscriptionGroup, String title);

    List<Subscription> listSubscriptions(Feed feed);

    List<Subscription> list(SubscriptionGroup group);

    int countSubscribers(Feed feed);

    int getMaxOrder(SubscriptionGroup group);

}
