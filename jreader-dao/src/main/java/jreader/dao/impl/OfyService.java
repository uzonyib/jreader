package jreader.dao.impl;

import jreader.domain.Archive;
import jreader.domain.Feed;
import jreader.domain.FeedEntry;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

class OfyService {

	static {
		factory().register(User.class);
		factory().register(SubscriptionGroup.class);
		factory().register(Subscription.class);
		factory().register(Feed.class);
		factory().register(FeedEntry.class);
		factory().register(Archive.class);
	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}

}
