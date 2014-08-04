package jreader.dao;

import java.util.List;

import jreader.domain.Archive;
import jreader.domain.ArchivedEntry;
import jreader.domain.User;

public interface ArchivedEntryDao extends OfyDao<ArchivedEntry> {
	
	List<ArchivedEntry> list(User user, ArchivedEntryFilter filter);
	
	List<ArchivedEntry> list(Archive archive, ArchivedEntryFilter filter);
	
}
