package jreader.dao.impl;

import jreader.dao.SubscriptionGroupDao;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;

@Repository
public class SubscriptionGroupDaoImpl implements SubscriptionGroupDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;

	@Override
	public SubscriptionGroup find(User user, String title) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(SubscriptionGroup.class).ancestor(user).filter("title =", title).first().now();
	}

	@Override
	public void save(final SubscriptionGroup subscriptionGroup) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entity(subscriptionGroup).now();
			}
		});
	}
	
	@Override
	public void delete(final SubscriptionGroup group) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.delete().entity(group).now();
			}
		});
	}

	@Override
	public int countSubscriptions(SubscriptionGroup group, User user) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).ancestor(user).filter("groupRef =", group).count();
	}

}
