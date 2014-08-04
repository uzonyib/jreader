package jreader.services;

import jreader.dao.ArchivedEntryFilter;

public class ArchivedEntryFilterData extends ArchivedEntryFilter {

	private String username;
	private Long archiveId;
	
	public ArchivedEntryFilterData(String username, boolean ascending, int offset, int count) {
		super(ascending, offset, count);
		this.username = username;
	}

	public ArchivedEntryFilterData(String username, Long archiveId, boolean ascending, int offset, int count) {
		super(ascending, offset, count);
		this.username = username;
		this.archiveId = archiveId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getArchiveId() {
		return archiveId;
	}

	public void setArchiveId(Long archiveId) {
		this.archiveId = archiveId;
	}

}
