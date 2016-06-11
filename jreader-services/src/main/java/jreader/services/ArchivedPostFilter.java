package jreader.services;

import lombok.Getter;

@Getter
public final class ArchivedPostFilter {

    private final jreader.dao.ArchivedPostFilter entityFilter;
    private final String username;
    private final Long archiveId;
    
    public ArchivedPostFilter(final String username, final Long archiveId, final boolean ascending, final int offset, final int count) {
        this.entityFilter = new jreader.dao.ArchivedPostFilter(ascending, offset, count);
        this.username = username;
        this.archiveId = archiveId;
    }

    public ArchivedPostFilter(final String username, final boolean ascending, final int offset, final int count) {
        this(username, null, ascending, offset, count);
    }

}
