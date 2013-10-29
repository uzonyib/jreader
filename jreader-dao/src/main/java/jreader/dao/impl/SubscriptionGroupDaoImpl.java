package jreader.dao.impl;

import java.util.List;

import jreader.dao.SubscriptionGroupDao;
import jreader.domain.Subscription;
import jreader.domain.SubscriptionGroup;
import jreader.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;

@Repository
public class SubscriptionGroupDaoImpl implements SubscriptionGroupDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;

	@Override
	public SubscriptionGroup find(User user, Long id) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(SubscriptionGroup.class).parent(user).id(id).now();
	}
	
	@Override
	public SubscriptionGroup find(User user, String title) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(SubscriptionGroup.class).ancestor(user).filter("title =", title).first().now();
	}

	@Override
	public SubscriptionGroup save(final SubscriptionGroup subscriptionGroup) {
		final Objectify ofy = objectifyFactory.begin();
		return ofy.transact(new Work<SubscriptionGroup>() {
			@Override
			public SubscriptionGroup run() {
				Key<SubscriptionGroup> key = ofy.save().entity(subscriptionGroup).now();
				return ofy.load().key(key).now();
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
	public int countSubscriptions(SubscriptionGroup group) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Subscription.class).ancestor(group.getUser()).filter("groupRef =", group).count();
	}
	
	@Override
	public int getMaxOrder(User user) {
		Objectify ofy = objectifyFactory.begin();
		SubscriptionGroup group = ofy.load().type(SubscriptionGroup.class).ancestor(user).order("-order").first().now();
		return group == null ? -1 : group.getOrder();
	}
	
	@Override
	public List<SubscriptionGroup> list(User user) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(SubscriptionGroup.class).ancestor(user).order("order").list();
	}

}
