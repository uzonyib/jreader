package jreader.services;

import java.util.List;

import jreader.dto.ArchiveDto;
import jreader.dto.ArchivedEntryDto;

public interface ArchiveService {
	
	ArchiveDto createArchive(String username, String title);
	
	void deleteArchive(String username, Long archiveId);
	
	void moveUp(String username, Long archiveId);
	
	void moveDown(String username, Long archiveId);
	
	List<ArchiveDto> list(String username);
	
	void entitle(String username, Long archiveId, String title);

	void archive(String username, Long groupId, Long subscriptionId, Long entryId, Long archiveId);
	
	List<ArchivedEntryDto> listEntries(ArchivedEntryFilterData filterData);
	
	void deleteEntry(String username, Long archiveId, Long entryId);
	
}
