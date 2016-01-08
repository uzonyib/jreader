package jreader.services;

public class ArchivedPostFilter extends jreader.dao.ArchivedPostFilter {

    private String username;
    private Long archiveId;

    public ArchivedPostFilter(final String username, final boolean ascending, final int offset, final int count) {
        super(ascending, offset, count);
        this.username = username;
    }

    public ArchivedPostFilter(final String username, final Long archiveId, final boolean ascending, final int offset, final int count) {
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
