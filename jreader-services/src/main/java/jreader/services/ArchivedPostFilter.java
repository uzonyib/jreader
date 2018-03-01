package jreader.services;

import org.springframework.data.domain.Pageable;

import lombok.Getter;

@Getter
public final class ArchivedPostFilter {

    private final String username;
    private final Long archiveId;
    private final Pageable page;

    public ArchivedPostFilter(final String username, final Long archiveId, final Pageable page) {
        this.username = username;
        this.archiveId = archiveId;
        this.page = page;
    }

    public ArchivedPostFilter(final String username, final Pageable page) {
        this(username, null, page);
    }

}
