package jreader.services;

import jreader.dao.ArchivedEntryFilter;

public class ArchivedEntryFilterData extends ArchivedEntryFilter {

    private String username;
    private Long archiveId;

    public ArchivedEntryFilterData(final String username, final boolean ascending, final int offset, final int count) {
        super(ascending, offset, count);
        this.username = username;
    }

    public ArchivedEntryFilterData(final String username, final Long archiveId, final boolean ascending, final int offset, final int count) {
        super(ascending, offset, count);
        this.username = username;
        this.archiveId = archiveId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Long getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(final Long archiveId) {
        this.archiveId = archiveId;
    }

}
