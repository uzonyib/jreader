package jreader.services;

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

    public PostFilter(final String username, final PostType postType, final boolean ascending, final int offset, final int count) {
        this(username, null, null, postType, ascending, offset, count, ParentType.USER);
    }

    public PostFilter(final String username, final Long groupId, final PostType postType, final boolean ascending, final int offset, final int count) {
        this(username, groupId, null, postType, ascending, offset, count, ParentType.GROUP);
    }

    public PostFilter(final String username, final Long groupId, final Long subscriptionId, final PostType postType, final boolean ascending, final int offset,
            final int count) {
        this(username, groupId, subscriptionId, postType, ascending, offset, count, ParentType.SUBSCRIPTION);
    }

    private PostFilter(final String username, final Long groupId, final Long subscriptionId, final PostType postType, final boolean ascending, final int offset,
            final int count, final ParentType parentType) {
        this.entityFilter = new jreader.dao.PostFilter(postType, ascending, offset, count);
        this.parentType = parentType;
        this.username = username;
        this.groupId = groupId;
        this.subscriptionId = subscriptionId;
    }

}
