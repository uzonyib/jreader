package jreader.services;

import jreader.dao.PostFilter;

public class PostFilterData extends PostFilter {

    public enum Vertical {
        ALL, GROUP, SUBSCRIPTION;
    }

    private String username;
    private Vertical vertical;
    private Long groupId;
    private Long subscriptionId;

    public PostFilterData(final String username, final PostType postType, final boolean ascending, final int offset, final int count) {
        super(postType, ascending, offset, count);
        this.username = username;
        this.vertical = Vertical.ALL;
    }

    public PostFilterData(final String username, final Long groupId, final PostType postType, final boolean ascending, final int offset, final int count) {
        super(postType, ascending, offset, count);
        this.username = username;
        this.groupId = groupId;
        this.vertical = Vertical.GROUP;
    }

    public PostFilterData(final String username, final Long groupId, final Long subscriptionId, final PostType postType, final boolean ascending,
            final int offset, final int count) {
        super(postType, ascending, offset, count);
        this.username = username;
        this.groupId = groupId;
        this.subscriptionId = subscriptionId;
        this.vertical = Vertical.SUBSCRIPTION;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Vertical getVertical() {
        return vertical;
    }

    public void setVertical(final Vertical type) {
        this.vertical = type;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(final Long groupId) {
        this.groupId = groupId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(final Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

}
