package jreader.dao.impl;

import jreader.domain.Archive;
import jreader.domain.Feed;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

public class EntityFactory {
	
	public SubscriptionGroup createGroup(User user, String title, int order) {
		SubscriptionGroup subscriptionGroup = new SubscriptionGroup();
		subscriptionGroup.setUser(user);
		subscriptionGroup.setTitle(title);
		subscriptionGroup.setOrder(order);
		return subscriptionGroup;
	}
	
	public Subscription createSubscription(SubscriptionGroup group, Feed feed, String title,
			int order, Long updatedDate, Long refreshDate) {
		Subscription subscription = new Subscription();
		subscription.setGroup(group);
		subscription.setFeed(feed);
		subscription.setTitle(title);
		subscription.setOrder(order);
		subscription.setUpdatedDate(updatedDate);
		subscription.setRefreshDate(refreshDate);
		return subscription;
	}
	
	public Archive createArchive(User user, String title, int order) {
		Archive archive = new Archive();
		archive.setUser(user);
		archive.setTitle(title);
		archive.setOrder(order);
		return archive;
	}

}
