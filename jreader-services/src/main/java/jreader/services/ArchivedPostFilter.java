package jreader.services;

public class ArchivedPostFilter {

    private jreader.dao.ArchivedPostFilter entityFilter;
    private String username;
    private Long archiveId;

    public ArchivedPostFilter(final String username, final boolean ascending, final int offset, final int count) {
        this.entityFilter = new jreader.dao.ArchivedPostFilter(ascending, offset, count);
        this.username = username;
    }

    public ArchivedPostFilter(final String username, final Long archiveId, final boolean ascending, final int offset, final int count) {
        this.entityFilter = new jreader.dao.ArchivedPostFilter(ascending, offset, count);
        this.username = username;
        this.archiveId = archiveId;
    }
    
    public jreader.dao.ArchivedPostFilter getEntityFilter() {
        return entityFilter;
    }

    public String getUsername() {
        return username;
    }

    public Long getArchiveId() {
        return archiveId;
    }

}
