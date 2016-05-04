package jreader.services;

import jreader.dao.PostFilter.PostType;

public class PostFilter {

    public enum ParentType {
        USER, GROUP, SUBSCRIPTION;
    }

    private jreader.dao.PostFilter entityFilter;
    private ParentType parentType;
    private String username;
    private Long groupId;
    private Long subscriptionId;

    public PostFilter(final String username, final PostType postType, final boolean ascending, final int offset, final int count) {
        this.entityFilter = new jreader.dao.PostFilter(postType, ascending, offset, count);
        this.parentType = ParentType.USER;
        this.username = username;
    }

    public PostFilter(final String username, final Long groupId, final PostType postType, final boolean ascending, final int offset, final int count) {
        this.entityFilter = new jreader.dao.PostFilter(postType, ascending, offset, count);
        this.parentType = ParentType.GROUP;
        this.username = username;
        this.groupId = groupId;
    }

    public PostFilter(final String username, final Long groupId, final Long subscriptionId, final PostType postType, final boolean ascending,
            final int offset, final int count) {
        this.entityFilter = new jreader.dao.PostFilter(postType, ascending, offset, count);
        this.parentType = ParentType.SUBSCRIPTION;
        this.username = username;
        this.groupId = groupId;
        this.subscriptionId = subscriptionId;
    }
    
    public jreader.dao.PostFilter getEntityFilter() {
        return entityFilter;
    }
    
    public ParentType getParentType() {
        return parentType;
    }

    public String getUsername() {
        return username;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

}
