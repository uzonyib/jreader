package jreader.dao.impl;

import java.util.List;

import jreader.dao.ArchivedEntryDao;
import jreader.dao.ArchivedEntryFilter;
import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.User;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.cmd.Query;

public class ArchivedEntryDaoImpl extends AbstractOfyDao<ArchivedEntry> implements ArchivedEntryDao {

	public ArchivedEntryDaoImpl(ObjectifyFactory objectifyFactory) {
		super(objectifyFactory);
	}
	
	@Override
	public ArchivedEntry find(Archive archive, Long id) {
		Objectify ofy = getOfy();
		return ofy.load().type(ArchivedEntry.class).parent(archive).id(id).now();
	}

	@Override
	public List<ArchivedEntry> list(User user, ArchivedEntryFilter filter) {
		return listForAncestor(user, filter);
	}

	@Override
	public List<ArchivedEntry> list(Archive archive, ArchivedEntryFilter filter) {
		return listForAncestor(archive, filter);
	}
	
	private List<ArchivedEntry> listForAncestor(Object ancestor, ArchivedEntryFilter filter) {
		Query<ArchivedEntry> query = getOfy().load().type(ArchivedEntry.class).ancestor(ancestor);
		return query.order(filter.isAscending() ? "publishedDate" : "-publishedDate").offset(filter.getOffset()).limit(filter.getCount()).list();
	}

}
