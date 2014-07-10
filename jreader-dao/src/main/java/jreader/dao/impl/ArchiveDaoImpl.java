package jreader.dao.impl;

import java.util.List;

import jreader.dao.ArchiveDao;
import jreader.domain.Archive;
import jreader.domain.User;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;

public class ArchiveDaoImpl extends AbstractOfyDao<Archive> implements ArchiveDao {
	
	public ArchiveDaoImpl(ObjectifyFactory objectifyFactory) {
		super(objectifyFactory);
	}
	
	@Override
	public Archive find(User user, Long id) {
		Objectify ofy = getOfy();
		return ofy.load().type(Archive.class).parent(user).id(id).now();
	}
	
	@Override
	public Archive find(User user, String title) {
		Objectify ofy = getOfy();
		return ofy.load().type(Archive.class).ancestor(user).filter("title =", title).first().now();
	}
	
	@Override
	public List<Archive> list(User user) {
		Objectify ofy = getOfy();
		return ofy.load().type(Archive.class).ancestor(user).order("order").list();
	}

	@Override
	public int getMaxOrder(User user) {
		Objectify ofy = getOfy();
		Archive archive = ofy.load().type(Archive.class).ancestor(user).order("-order").first().now();
		return archive == null ? -1 : archive.getOrder();
	}

}
