package jreader.dao.impl;

import java.util.List;

import jreader.dao.ActionDao;
import jreader.domain.Action;
import jreader.domain.FeedEntry;
import jreader.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.VoidWork;

@Repository
public class ActionDaoImpl implements ActionDao {
	
	@Autowired
	private ObjectifyFactory objectifyFactory;

	@Override
	public void save(final Action action) {
		final Objectify ofy = objectifyFactory.begin();
		ofy.transact(new VoidWork() {
			@Override
			public void vrun() {
				ofy.save().entity(action).now();
			}
		});
	}
	
	@Override
	public Action find(User user, FeedEntry feedEntry, String type) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Action.class).ancestor(user).filter("feedEntryRef =", feedEntry).filter("type =", type).first().now();
	}
	
	@Override
	public boolean isRead(User user, FeedEntry feedEntry) {
		return find(user, feedEntry, READ_ACTION_TYPE) != null;
	}

	@Override
	public List<Action> list(User user, String type) {
		Objectify ofy = objectifyFactory.begin();
		return ofy.load().type(Action.class).ancestor(user).filter("type =", type).list();
	}

}
