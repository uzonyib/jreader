package jreader.services;

import jreader.dao.PostFilter.PostType;

public class PostFilter {

    public enum Vertical {
        ALL, GROUP, SUBSCRIPTION;
    }

    private jreader.dao.PostFilter entityFilter;
    private Vertical vertical;
    private String username;
    private Long groupId;
    private Long subscriptionId;

    public PostFilter(final String username, final PostType postType, final boolean ascending, final int offset, final int count) {
        this.entityFilter = new jreader.dao.PostFilter(postType, ascending, offset, count);
        this.vertical = Vertical.ALL;
        this.username = username;
    }

    public PostFilter(final String username, final Long groupId, final PostType postType, final boolean ascending, final int offset, final int count) {
        this.entityFilter = new jreader.dao.PostFilter(postType, ascending, offset, count);
        this.vertical = Vertical.GROUP;
        this.username = username;
        this.groupId = groupId;
    }

    public PostFilter(final String username, final Long groupId, final Long subscriptionId, final PostType postType, final boolean ascending,
            final int offset, final int count) {
        this.entityFilter = new jreader.dao.PostFilter(postType, ascending, offset, count);
        this.vertical = Vertical.SUBSCRIPTION;
        this.username = username;
        this.groupId = groupId;
        this.subscriptionId = subscriptionId;
    }
    
    public jreader.dao.PostFilter getEntityFilter() {
        return entityFilter;
    }
    
    public Vertical getVertical() {
        return vertical;
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
