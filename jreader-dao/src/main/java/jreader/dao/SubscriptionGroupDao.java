package jreader.dao;

import java.util.List;

import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public interface SubscriptionGroupDao extends OfyDao<SubscriptionGroup> {

    SubscriptionGroup find(User user, Long id);

    SubscriptionGroup find(User user, String title);

    List<SubscriptionGroup> list(User user);

    int getMaxOrder(User user);

}
