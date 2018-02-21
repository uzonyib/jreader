package jreader.services;

import org.springframework.data.domain.Pageable;

import jreader.dao.PostFilter.PostType;
import lombok.Value;

@Value
public final class PostFilter {

    public enum ParentType {
        USER, GROUP, SUBSCRIPTION;
    }

    private final jreader.dao.PostFilter entityFilter;
    private final ParentType parentType;
    private final String username;
    private final Long groupId;
    private final Long subscriptionId;

    public PostFilter(final String username, final PostType postType, final Pageable page) {
        this(username, null, null, postType, page, ParentType.USER);
    }

    public PostFilter(final String username, final Long groupId, final PostType postType, final Pageable page) {
        this(username, groupId, null, postType, page, ParentType.GROUP);
    }

    public PostFilter(final String username, final Long groupId, final Long subscriptionId, final PostType postType, final Pageable page) {
        this(username, groupId, subscriptionId, postType, page, ParentType.SUBSCRIPTION);
    }

    private PostFilter(final String username, final Long groupId, final Long subscriptionId, final PostType postType, final Pageable page,
            final ParentType parentType) {
        this.entityFilter = new jreader.dao.PostFilter(postType, page);
        this.parentType = parentType;
        this.username = username;
        this.groupId = groupId;
        this.subscriptionId = subscriptionId;
    }

}
