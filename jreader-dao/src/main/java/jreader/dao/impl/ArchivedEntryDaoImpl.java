package jreader.dao.impl;

import jreader.dao.ArchivedEntryDao;
import jreader.domain.ArchivedEntry;

import com.googlecode.objectify.ObjectifyFactory;

public class ArchivedEntryDaoImpl extends AbstractOfyDao<ArchivedEntry> implements ArchivedEntryDao {

	public ArchivedEntryDaoImpl(ObjectifyFactory objectifyFactory) {
		super(objectifyFactory);
	}

}
